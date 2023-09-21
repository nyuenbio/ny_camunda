package com.nyuen.camunda.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nyuen.camunda.common.PageBean;
import com.nyuen.camunda.domain.po.ExperimentData;
import com.nyuen.camunda.domain.po.NyuenResultCheck;
import com.nyuen.camunda.domain.vo.*;
import com.nyuen.camunda.mapper.NyuenResultCheckMapper;
import com.nyuen.camunda.result.Result;
import com.nyuen.camunda.result.ResultFactory;
import com.nyuen.camunda.service.ExperimentDataService;
import com.nyuen.camunda.service.MyTaskService;
import com.nyuen.camunda.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Attachment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2022/10/24
 */
@Api(tags = "其他流程操作控制类")
@RestController
@RequestMapping("/process")
public class ProcessController {
    @Resource
    private MyTaskService myTaskService;
    @Resource
    private TaskService taskService;
    @Resource
    private IdentityService identityService;
    @Resource
    private ExperimentDataService experimentDataService;
    @Resource
    private NyuenResultCheckMapper nyuenResultCheckMapper;

    @Value("${file.saveRootPath}")
    private String saveRootPath;
    @Value("${file.readRootPath}")
    private String readRootPath;
    @Value("${operationLogService}")
    private String operationLogService;


    @ApiOperation(value = "根据样本编号查询流程",httpMethod = "POST")
    @PostMapping("/getSampleProcessList")
    public Result getSampleProcessList(@RequestBody SimpleQueryBean sqBean) throws IllegalAccessException {
        if(sqBean == null){
            return ResultFactory.buildFailResult("参数不能为空");
        }
        if(StringUtils.isEmpty(sqBean.getName())) {
            // 查询全部
            sqBean.setName("");
        }
        Map<String,Object> params =  ObjectUtil.objectToMap(sqBean);
        PageConvert.currentPageConvertStartIndex(params);
        return ResultFactory.buildSuccessResult(myTaskService.getSampleProcessList(params));
    }

    /**
     * 通过上传解析Excel表格，处理流程节点并添加流程变量
     * @param multipartFile 表格文件
     * @param assignee 当前用户id
     * @param procDefId 流程模板id
     * @param nodeName 节点名称
     * @return 处理样本结果
     * @throws IOException IOException
     * @throws InvalidFormatException InvalidFormatException
     */
    @ApiOperation(value = "通过上传解析Excel表格，处理流程节点并添加流程变量", httpMethod = "POST")
    @PostMapping("/batchDealNodeByExcel")
    public Result batchDealNodeByExcel(MultipartFile multipartFile, @ApiParam("当前登录人id") String assignee,
                                       String procDefId, @ApiParam("节点名称") String nodeName, HttpServletRequest request)
            throws IOException, InvalidFormatException {
        if(StringUtils.isEmpty(assignee)){
            return ResultFactory.buildFailResult("当前用户不能为空");
        }
        if(StringUtils.isEmpty(nodeName)){
            return ResultFactory.buildFailResult("节点名称不能为空");
        }
        if(StringUtils.isEmpty(procDefId)){
            return ResultFactory.buildFailResult("流程不能为空");
        }
        // 上传抽提数据,或上传下机数据
        Result result1 = ExcelUtil.dealDataByExcel(multipartFile);
        if(200 != result1.getCode()){
            return result1;
        }
        // 得到相同样本编号归类后的Excel表格数据
        List<SampleRowAndCell> sampleRowAndCellList = (List<SampleRowAndCell>) result1.getData();
        // 1、校验表格 V4版本规则
        Result checkResult = experimentDataCheckV42(sampleRowAndCellList, nodeName, assignee, multipartFile.getOriginalFilename(),request);
        if(StringUtils.isNotEmpty(checkResult.getMessage())){
            return checkResult;
        }
        // 2、存储Excel数据文件
        //if(StringUtils.equalsIgnoreCase(nodeName.trim(),"下机")) {
        String fileReadPath = FileUtil.uploadFile(multipartFile, saveRootPath, readRootPath);
        ExperimentData experimentData = new ExperimentData();
        experimentData.setProcDefId(procDefId);
        experimentData.setNodeName(nodeName);
        experimentData.setFileName(multipartFile.getOriginalFilename());
        experimentData.setUrl(fileReadPath);
        experimentData.setCreateUser(assignee);
        experimentData.setCreateTime(new Date());
        experimentData.setStatus(0+"");
        experimentDataService.addExperimentData(experimentData);
        //}
        // 3、处理流程节点
        return dealTaskNodeByExcel(sampleRowAndCellList,assignee,procDefId,nodeName);
        //return ResultFactory.buildSuccessResult("");
    }

    private Result experimentDataCheckV3(List<SampleRowAndCell> sampleRowAndCellList,String nodeName){
        if(!StringUtils.equalsIgnoreCase(nodeName.trim(),"下机")){
            return ResultFactory.buildResult(200,"",null);
        }
        List<String> gelGenoTypeArray = Arrays.asList("SS", "SL", "SXL", "LL", "LXL");
        List<String> descriptionArray = Arrays.asList("D.LOW Probability", "N.No-Alleles");
        // 校验规则
        // 1.每个样本现在是91个位点（有漏的点提示）
        // 2.Methodology-CNV的位点，固定是rs16947(可能会因为下拉导致错误)
        // 3. SS/SL/SXL/LL/SXL/LXL-对应的Methodology是GEL
        // 4. Description这一列评级不能出现D.LOW Probability以及N.No-Alleles
        StringBuilder siteErr = new StringBuilder();
        List<String> cnvErr = new ArrayList<>();
        List<String> gelErr = new ArrayList<>();
        List<String> descriptionErr = new ArrayList<>();
        List<ExperimentalDataRow> experimentalDataRowList = new ArrayList<>();
        for(SampleRowAndCell sampleRowAndCell: sampleRowAndCellList){
            List<List<String>> sampleRowList = sampleRowAndCell.getSampleRowList();
            for(List<String> rowList : sampleRowList){
                ExperimentalDataRow dataRow = new ExperimentalDataRow();
                dataRow.setSampleNum(sampleRowAndCell.getSampleInfo());
                dataRow.setGenotype(rowList.get(1));
                dataRow.setSnpId(rowList.get(2));
                dataRow.setDescription(rowList.get(3));
                dataRow.setMethodology(rowList.get(5));
                experimentalDataRowList.add(dataRow);
            }
            if(91 != sampleRowList.size()){
                siteErr.append(sampleRowAndCell.getSampleInfo()).append(" ,");
            }
        }
        experimentalDataRowList.stream().filter(experimentalDataRow -> "CNV".equals(experimentalDataRow.getMethodology()) && !"rs16947".equals(experimentalDataRow.getSnpId()))
                .collect(Collectors.toList())
                .stream().map(experimentalDataRow -> {
                    cnvErr.add(experimentalDataRow.getSampleNum());
                    return cnvErr;
                }).collect(Collectors.toList());
        experimentalDataRowList.stream().filter(experimentalDataRow -> "GEL".equals(experimentalDataRow.getMethodology()) && !(gelGenoTypeArray.indexOf(experimentalDataRow.getGenotype()) >= 0))
                .collect(Collectors.toList())
                .stream().map(experimentalDataRow -> {
                    gelErr.add(experimentalDataRow.getSampleNum());
                    return gelErr;
                }).collect(Collectors.toList());
        experimentalDataRowList.stream().filter(experimentalDataRow -> descriptionArray.indexOf(experimentalDataRow.getDescription()) >= 0)
                .collect(Collectors.toList())
                .stream().map(experimentalDataRow -> {
                    descriptionErr.add(experimentalDataRow.getSampleNum());
                    return descriptionErr;
                }).collect(Collectors.toList());

        String result = (siteErr.length()>0?siteErr.append("以上样本位点不为91个！").toString():"")
                +(cnvErr.size()>0? cnvErr.stream().distinct().collect(Collectors.toList()).toString()+"以上样本存在CNV位点不是rs16947 。":"")
                +(gelErr.size()>0?gelErr.stream().distinct().collect(Collectors.toList()).toString()+"以上样本存在GEL位点不是SS/SL/SXL/LL/LXL 。":"")
                +(descriptionErr.size()>0?descriptionErr.stream().distinct().collect(Collectors.toList()).toString()+"以上样本存在description是D.LOW Probability或N.No-Alleles 。":"");
        if(StringUtils.isEmpty(result)){
            return ResultFactory.buildResult(200,"",null);
        }
        return ResultFactory.buildResult(200,result,null);
    }

    private Result experimentDataCheckV4(List<SampleRowAndCell> sampleRowAndCellList,String nodeName){
        if(!StringUtils.equalsIgnoreCase(nodeName.trim(),"下机")){
            return ResultFactory.buildResult(200,"",null);
        }
        List<String> gelGenoTypeArray = Arrays.asList("SS", "SL", "SXL", "LL", "LXL");
        List<String> descriptionArray = Collections.singletonList("D.LOW Probability");
        // 校验规则
        //
        // 2.Methodology-CNV的位点，固定是rs16947(可能会因为下拉导致错误)
        // 3. SS/SL/SXL/LL/SXL/LXL-对应的Methodology是GEL
        // 4. Description这一列评级不能出现D.LOW Probability
        List<String> cnvErr = new ArrayList<>();
        List<String> gelErr = new ArrayList<>();
        List<String> descriptionErr = new ArrayList<>();
        List<ExperimentalDataRow> experimentalDataRowList = new ArrayList<>();
        for(SampleRowAndCell sampleRowAndCell: sampleRowAndCellList){
            List<List<String>> sampleRowList = sampleRowAndCell.getSampleRowList();
            for(List<String> rowList : sampleRowList){
                ExperimentalDataRow dataRow = new ExperimentalDataRow();
                dataRow.setSampleNum(sampleRowAndCell.getSampleInfo());
                dataRow.setGenotype(rowList.get(1));
                dataRow.setSnpId(rowList.get(2));
                dataRow.setDescription(rowList.get(3));
                dataRow.setMethodology(rowList.get(5));
                experimentalDataRowList.add(dataRow);
            }
        }
        experimentalDataRowList.stream().filter(experimentalDataRow -> "CNV".equals(experimentalDataRow.getMethodology()) && !"rs16947".equals(experimentalDataRow.getSnpId()))
                .collect(Collectors.toList())
                .stream().map(experimentalDataRow -> {
            cnvErr.add(experimentalDataRow.getSampleNum());
            return cnvErr;
        }).collect(Collectors.toList());
        experimentalDataRowList.stream().filter(experimentalDataRow -> "GEL".equals(experimentalDataRow.getMethodology()) && !(gelGenoTypeArray.indexOf(experimentalDataRow.getGenotype()) >= 0))
                .collect(Collectors.toList())
                .stream().map(experimentalDataRow -> {
            gelErr.add(experimentalDataRow.getSampleNum());
            return gelErr;
        }).collect(Collectors.toList());
        experimentalDataRowList.stream().filter(experimentalDataRow -> descriptionArray.indexOf(experimentalDataRow.getDescription()) >= 0)
                .collect(Collectors.toList())
                .stream().map(experimentalDataRow -> {
            descriptionErr.add(experimentalDataRow.getSampleNum());
            return descriptionErr;
        }).collect(Collectors.toList());

        String result = (cnvErr.size()>0? cnvErr.stream().distinct().collect(Collectors.toList()).toString()+"以上样本存在CNV位点不是rs16947 。" : "")
                +(gelErr.size()>0?gelErr.stream().distinct().collect(Collectors.toList()).toString()+"以上样本存在GEL位点不是SS/SL/SXL/LL/LXL 。" : "")
                +(descriptionErr.size()>0?descriptionErr.stream().distinct().collect(Collectors.toList()).toString()+"以上样本存在description是D.LOW Probability 。" : "");
        if(StringUtils.isEmpty(result)){
            return ResultFactory.buildResult(200,"",null);
        }
        return ResultFactory.buildResult(200,result,null);
    }

    private Result experimentDataCheckV42(List<SampleRowAndCell> sampleRowAndCellList,String nodeName,String assignee, String fileName,HttpServletRequest request){
        if(!StringUtils.equalsIgnoreCase(nodeName.trim(),"下机")){
            return ResultFactory.buildResult(200,"",null);
        }
        List<String> descriptionArray = Collections.singletonList("D.LOW Probability");
        // 校验规则
        // 2.Methodology-CNV的位点，固定是rs16947(可能会因为下拉导致错误)
        // 4.Description这一列评级不能出现D.LOW Probability
        // 5.同一样本，同一rs号，如果call不同，需记录并报错
        // 6.需根据套餐校验位点数，A:29,B:20,C:29,D:27,E:30,F:25,G:25,CNV:1
        List<String> cnvErr = new ArrayList<>();
        List<String> descriptionErr = new ArrayList<>();
        StringBuilder holeNumErr = new StringBuilder();
        StringBuilder callResultErr = new StringBuilder();
        Set<String> callResultErrSamples = new HashSet<>();
        List<ExperimentalDataRowNew> experimentalDataRowList = new ArrayList<>();
        // 避免再次上传时重复数据 todo
        // （1）根据样本编号批量删除nyuenResultCheck表
        nyuenResultCheckMapper.delResultBySampleInfo(sampleRowAndCellList);
        for(SampleRowAndCell sampleRowAndCell: sampleRowAndCellList){
            List<List<String>> sampleRowList = sampleRowAndCell.getSampleRowList();
            for(List<String> rowList : sampleRowList){
                ExperimentalDataRowNew dataRowNew = new ExperimentalDataRowNew();
                dataRowNew.setSampleNum(sampleRowAndCell.getSampleInfo());
                dataRowNew.setCallResult(rowList.get(0));
                dataRowNew.setAssayId(rowList.get(1));
                dataRowNew.setWellPosition(rowList.get(2));
                dataRowNew.setDescription(rowList.get(3));
                dataRowNew.setGeneHap(rowList.get(4));
                dataRowNew.setMethodology(rowList.get(5));
                experimentalDataRowList.add(dataRowNew);
                NyuenResultCheck resultCheck = new NyuenResultCheck();
                resultCheck.setSampleInfo(sampleRowAndCell.getSampleInfo());
                resultCheck.setCallResult(rowList.get(0));
                resultCheck.setAssayId(rowList.get(1));
                resultCheck.setAssayType(rowList.get(1).substring(0,1));
                resultCheck.setCreateUser(assignee);
                resultCheck.setCreateTime(new Date());
                //检测结果取值：SNP取得是call这一栏，CNV需要取gene Hap这一栏
                if(null != rowList.get(6) && "CNV".equalsIgnoreCase(rowList.get(5).trim())){
                    resultCheck.setAssayType("CNV");
                    resultCheck.setCallResult(rowList.get(4));
                }
                nyuenResultCheckMapper.insertSelective(resultCheck);
            }
        }
        // （2）批量更新nyuenResultCheck表的rs号 todo 添加事务?
        nyuenResultCheckMapper.updateSnpInfo(sampleRowAndCellList);
        // （3）校验相同rs号的结果，校验各类型孔位的位点数
        checkHoleNumAndCallResult(sampleRowAndCellList,holeNumErr,callResultErrSamples,callResultErr);
        if(callResultErr.length()>0) {
            Map<String, Object> map = new HashMap<>();
            map.put("sampleNum", callResultErrSamples.toString().substring(1,callResultErrSamples.toString().length()-1));
            map.put("operation", "上传下机数据" + fileName);
            map.put("type", 2);
            map.put("undefined1", callResultErr.toString());
            map.put("createTime", new Date());
            map.put("Authorization",request.getHeader("Authorization"));
            String result = HttpClientUtil.httpPostJson(operationLogService, JSON.parseObject(JSONObject.toJSONString(map)));
            JSONObject jsonObject = JSON.parseObject(result);
            if(null != jsonObject.get("code") && !"200".equals(jsonObject.get("code").toString())){
                return ResultFactory.buildFailResult(jsonObject.get("message").toString());
            }
            if(null != jsonObject.get("status") && "500".equals(jsonObject.get("status").toString())){
                return ResultFactory.buildFailResult(jsonObject.get("message").toString());
            }
        }
        experimentalDataRowList.stream().filter(experimentalDataRow -> "CNV".equals(experimentalDataRow.getMethodology()) && !"rs16947".equals(experimentalDataRow.getAssayId()))
                .collect(Collectors.toList())
                .stream().map(experimentalDataRow -> {
            cnvErr.add(experimentalDataRow.getSampleNum());
            return cnvErr;
        }).collect(Collectors.toList());

        experimentalDataRowList.stream().filter(experimentalDataRow -> descriptionArray.indexOf(experimentalDataRow.getDescription()) >= 0)
                .collect(Collectors.toList())
                .stream().map(experimentalDataRow -> {
            descriptionErr.add(experimentalDataRow.getSampleNum());
            return descriptionErr;
        }).collect(Collectors.toList());

        String result = (cnvErr.size()>0? cnvErr.stream().distinct().collect(Collectors.toList()).toString()+"以上样本存在CNV位点不是rs16947 。" : "")
                +(descriptionErr.size()>0?descriptionErr.stream().distinct().collect(Collectors.toList()).toString()+"以上样本存在description是D.LOW Probability 。" : "")
                +(holeNumErr.length()>0? holeNumErr.toString():"")
                +(callResultErr.length()>0?callResultErr.toString():"");
        if(StringUtils.isEmpty(result)){
            return ResultFactory.buildResult(200,"",null);
        }
        return ResultFactory.buildResult(200,result,null);
    }

    private void checkHoleNumAndCallResult(List<SampleRowAndCell> sampleRowAndCellList, StringBuilder holeNumErr,
                                           Set<String> callResultErrSamples,StringBuilder callResultErr ){
        List<NyuenResultCheck> nyuenResultCheckList = nyuenResultCheckMapper.getSnpInfoBySampleNums(sampleRowAndCellList);
        Map<String,List<NyuenResultCheck>> groupMap = new HashMap<>();
        //将结果集按样本编号分组
        for(NyuenResultCheck resultCheck : nyuenResultCheckList){
            List<NyuenResultCheck> groupList = groupMap.get(resultCheck.getSampleInfo());
            if(null == groupList){
                List<NyuenResultCheck> valueList = new ArrayList<>();
                valueList.add(resultCheck);
                groupMap.put(resultCheck.getSampleInfo(),valueList);
            }else{
                groupList.add(resultCheck);
                groupMap.put(resultCheck.getSampleInfo(),groupList);
            }
        }
        for(Map.Entry entry : groupMap.entrySet()) {
            List<NyuenResultCheck> groupResultCheckList = (List<NyuenResultCheck>) entry.getValue();
            int a = 0;
            int b = 0;
            int c = 0;
            int d = 0;
            int e = 0;
            int f = 0;
            int g = 0;
            int cnv = 0;
            for(NyuenResultCheck resultCheck : groupResultCheckList){
                switch (resultCheck.getAssayType()){
                    case "A":
                        a=a+1;
                        break;
                    case "B":
                        b=b+1;
                        break;
                    case "C":
                        c=c+1;
                        break;
                    case "D":
                        d=d+1;
                        break;
                    case "E":
                        e=e+1;
                        break;
                    case "F":
                        f=f+1;
                        break;
                    case "G":
                        g=g+1;
                        break;
                    case "CNV":
                        cnv=cnv+1;
                        break;
                    default:
                        break;
                }
            }
            StringBuilder sampleHoleNumErr = new StringBuilder();
            boolean flag = false;
            if(a !=0 && AssayHoleNum.A.getHoleNum() != a){
                flag = true;
                sampleHoleNumErr.append(" A 孔位数量不为").append(AssayHoleNum.A.getHoleNum()).append(",");
            }
            if(b != 0 && AssayHoleNum.B.getHoleNum() != b){
                flag = true;
                sampleHoleNumErr.append(" B 孔位数量不为").append(AssayHoleNum.B.getHoleNum()).append(",");
            }
            if(c != 0 && AssayHoleNum.C.getHoleNum() != c){
                flag = true;
                sampleHoleNumErr.append(" C 孔位数量不为").append(AssayHoleNum.C.getHoleNum()).append(",");
            }
            if(d != 0 && AssayHoleNum.D.getHoleNum() != d){
                flag = true;
                sampleHoleNumErr.append(" D 孔位数量不为").append(AssayHoleNum.D.getHoleNum()).append(",");
            }
            if(e != 0 && AssayHoleNum.E.getHoleNum() != e){
                flag = true;
                sampleHoleNumErr.append(" E 孔位数量不为").append(AssayHoleNum.E.getHoleNum()).append(",");
            }
            if(f != 0 && AssayHoleNum.F.getHoleNum() != f){
                flag = true;
                sampleHoleNumErr.append(" F 孔位数量不为").append(AssayHoleNum.F.getHoleNum()).append(",");
            }
            if(g != 0 && AssayHoleNum.G.getHoleNum() != g){
                flag = true;
                sampleHoleNumErr.append(" G 孔位数量不为").append(AssayHoleNum.G.getHoleNum()).append(",");
            }
            if(cnv != 0 && AssayHoleNum.CNV.getHoleNum() != cnv){
                flag = true;
                sampleHoleNumErr.append(" CNV 孔位数量不为").append(AssayHoleNum.CNV.getHoleNum()).append(",");
            }
            if(flag){
                sampleHoleNumErr.insert(0,entry.getKey().toString());
                holeNumErr.append(sampleHoleNumErr);
            }
        }
        List<NyuenResultCheck> errorList = nyuenResultCheckMapper.getCallResultErrorBySampleNums(sampleRowAndCellList);
        if(null != errorList && errorList.size()>0){
            for(NyuenResultCheck resultCheck : errorList){
                String[] callResults = resultCheck.getCallResult().split(",");
                LinkedHashSet unDumpCall = new LinkedHashSet();
                unDumpCall.addAll(Arrays.asList(callResults));
                if(unDumpCall.size()>1) {
                    callResultErrSamples.add(resultCheck.getSampleInfo());
                    callResultErr.append("样本 ").append(resultCheck.getSampleInfo()).append(" AssayId为 ").append(resultCheck.getAssayId())
                            .append(" 存在多种检测结果： ").append(resultCheck.getCallResult()).append(" 。");
                }
            }
        }
    }

    private Result dealTaskNodeByExcel(List<SampleRowAndCell> sampleRowAndCellList, String assignee, String procDefId, String nodeName){
        StringBuilder sb = new StringBuilder();
        for(SampleRowAndCell sampleRowAndCell : sampleRowAndCellList){
            //添加审批人
            if(StringUtils.isNotEmpty(assignee)) {
                identityService.setAuthenticatedUserId(assignee);
            }
            // 根据样本编号（businessKey)、procDefId、nodeName和assignee找到taskId
            Map<String,Object> params = new HashMap<>();
            params.put("assignee",assignee);
            params.put("sampleInfo", sampleRowAndCell.getSampleInfo());
            params.put("procDefId",procDefId);
            params.put("nodeName",nodeName);
            List<TodoTask> todoTaskList = myTaskService.getTodoTaskByCondition(params);
            if(null == todoTaskList || todoTaskList.size() == 0){
                sb.append(sampleRowAndCell.getSampleInfo()).append(" , ");
            }
//            else if(todoTaskList.size() > 1){
//                return ResultFactory.buildFailResult("样本 "+sampleRowAndCell.getSampleInfo()+" 存在两条待办流程，无法匹配！");
//            }
            else {
                for(TodoTask todoTask : todoTaskList) {
                    String taskId = todoTask.getId();
                    // 添加流程变量
                    Map<String, Object> variables = new HashMap<>();
                    //variables.put("节点名称", nodeName);
                    //variables.put("样本编号", sampleRowAndCell.getSampleInfo());
                    variables.put(nodeName + "表格数据", JSON.toJSON(sampleRowAndCell.getSampleRowList()));
                    taskService.setVariablesLocal(taskId, variables);
                    taskService.complete(taskId);
                }
            }
        }
        return ResultFactory.buildResult(200,"".equals(sb.toString().trim()) ? "全部处理完成" : sb.toString()+"以上样本匹配失败,原因：您的待办节点中无此样本",null);
    }

    //@PostMapping("/myBatchDeal") 2023-08-10手动批量处理流程
    public Result myBatchDeal(@RequestBody MyBatchDealVo myBatchDealVo){
        StringBuilder sb = new StringBuilder();
        for(String sampleNum : myBatchDealVo.getSampleNumList()){
            //添加审批人
            if(StringUtils.isNotEmpty(myBatchDealVo.getAssignee())) {
                identityService.setAuthenticatedUserId(myBatchDealVo.getAssignee());
            }
            // 根据样本编号（businessKey)、procDefId、nodeName和assignee找到taskId
            Map<String,Object> params = new HashMap<>();
            params.put("assignee",myBatchDealVo.getAssignee());
            params.put("sampleInfo", sampleNum);
            params.put("procDefId",myBatchDealVo.getProcDefId());
            params.put("nodeName",myBatchDealVo.getNodeName());
            List<TodoTask> todoTaskList = myTaskService.getTodoTaskByCondition(params);
            if(todoTaskList.size() == 0){
                sb.append(sampleNum).append(" , ");
            }else if(todoTaskList.size() > 1){
                return ResultFactory.buildFailResult("样本 "+sampleNum+" 存在两条待办流程，无法匹配！");
            }else {
                String taskId = todoTaskList.get(0).getId();
                // 添加流程变量
                Map<String, Object> variables = new HashMap<>();
                variables.put("nodeName", myBatchDealVo.getNodeName());
                variables.put("remark","2023-08-10手动批量处理流程："+sampleNum);
                taskService.setVariablesLocal(taskId, variables);
                taskService.complete(taskId);
            }
        }
        return ResultFactory.buildResult(200,"".equals(sb.toString().trim()) ? "全部处理完成" : sb.toString()+"以上样本匹配失败,原因：您的待办节点中无此样本",null);
    }

    @ApiOperation(value = "批量处理‘数据确认’节点（或其他无变量节点）", httpMethod = "POST")
    @PostMapping("/batchDealWithNoVariables")
    public Result batchDealWithNoVariables(@RequestBody MyBatchDealVo myBatchDealVo){
        StringBuilder sb = new StringBuilder();
        for(String sampleNum : myBatchDealVo.getSampleNumList()){
            //添加审批人
            if(StringUtils.isNotEmpty(myBatchDealVo.getAssignee())) {
                identityService.setAuthenticatedUserId(myBatchDealVo.getAssignee());
            }
            // 根据样本编号（businessKey)、procDefId、nodeName和assignee找到taskId
            Map<String,Object> params = new HashMap<>();
            params.put("assignee",myBatchDealVo.getAssignee());
            params.put("sampleInfo", sampleNum);
            params.put("procDefId",myBatchDealVo.getProcDefId());
            params.put("nodeName",myBatchDealVo.getNodeName());
            List<TodoTask> todoTaskList = myTaskService.getTodoTaskByCondition(params);
            if(todoTaskList.size() == 0){
                sb.append(sampleNum).append(" , ");
            }else {
                // 存在两条以上待办流程，一起处理
                for(TodoTask todoTask : todoTaskList) {
                    String taskId = todoTask.getId();
                    // 添加流程变量
                    Map<String, Object> variables = new HashMap<>();
                    variables.put("nodeName", myBatchDealVo.getNodeName());
                    variables.put("remark", sampleNum);
                    taskService.setVariablesLocal(taskId, variables);
                    taskService.complete(taskId);
                }
            }
        }
        return ResultFactory.buildResult(200,"".equals(sb.toString().trim()) ? "全部处理完成" : sb.toString()+"以上样本匹配失败,原因：您的待办节点中无此样本",null);
    }

    @ApiOperation(value = "处理流程节点(带附件的节点)",httpMethod = "POST")
    @PostMapping("/dealTask")
    public Result dealTask(@RequestBody DealTaskBean dealTaskBean){
        //添加审批人
        if(StringUtils.isNotEmpty(dealTaskBean.getAssignee())) {
            identityService.setAuthenticatedUserId(dealTaskBean.getAssignee());
        }
        //添加审批意见，可在Act_Hi_Comment里的message查询到
        //三个参数分别为待办任务ID,流程实例ID,审批意见
        if(null != dealTaskBean.getComment()) {
            taskService.createComment(dealTaskBean.getTaskId(), dealTaskBean.getProcInstId(), dealTaskBean.getComment());
        }
        //方式一： 存文件流
        // createAttachment​(String attachmentType, String taskId, String processInstanceId,
        //          String attachmentName, String attachmentDescription, java.io.InputStream content)
        //String byteStr = dealTaskBean.getVariables().get("file").toString();
        //byte[] byteArr = byteStr.getBytes();
        //taskService.createAttachment("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet","b7900b4a-542f-11ed-9fc1-8cec4b96733f",
        //        "1b354cad-542e-11ed-9fc1-8cec4b96733f","一个附件文件","", new ByteArrayInputStream(byteArr));
        // 方式二：存url   ==>   http://report.nyuen-group.com:8077/sign/%E5%BC%A0%E4%B8%89.png
        // createAttachment​(String attachmentType, String taskId, String processInstanceId,
        //          String attachmentName, String attachmentDescription, String url)
        //处理节点变量
        if(null != dealTaskBean.getVariables()) {
            taskService.setVariables(dealTaskBean.getTaskId(), dealTaskBean.getVariables());
        }
        //处理附件文件
        if(null != dealTaskBean.getAttachmentVoList()) {
            for(AttachmentVo attachmentVo : dealTaskBean.getAttachmentVoList()) {
                taskService.createAttachment(attachmentVo.getType(), dealTaskBean.getTaskId(),
                        dealTaskBean.getProcInstId(), attachmentVo.getName(), attachmentVo.getDescription(), attachmentVo.getUrl());
            }
        }
        taskService.complete(dealTaskBean.getTaskId());//任务完成,也就是审批通过
        return ResultFactory.buildSuccessResult(null);
    }

    @ApiOperation(value = "处理流程节点(带全局变量的节点)",httpMethod = "POST")
    @PostMapping("/getAttachment")
    public Object getAttachment(String taskId) throws IOException {
        Map<String,Object> map = new HashMap<>();
        List<Map<String,Object>> resultList = new ArrayList<>();
        List<Attachment> attachmentList = taskService.getTaskAttachments(taskId);
        for(Attachment attachment : attachmentList){
            String attachmentName = attachment.getName();

            String url = attachment.getUrl();
            map.put("attachmentName",attachmentName);
            InputStream attachmentContent = taskService.getAttachmentContent(attachment.getId());
            if(attachmentContent != null ) {
                ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
                byte[] buff = new byte[1024];
                int rc = 0;
                while ((rc = attachmentContent.read(buff, 0, 1024)) > 0) {
                    swapStream.write(buff, 0, rc);
                }
                map.put("attachmentContent", swapStream.toByteArray());
            }
            map.put("url",url);
            resultList.add(map);
        }

        return resultList;
    }

    @ApiOperation(value = "获取下机数据文件列表",httpMethod = "POST")
    @PostMapping("/getExperimentDataList")
    public Object getExperimentDataList(@RequestBody SimpleQueryBean sqBean) throws IllegalAccessException {
        sqBean.setName("下机");
        Map<String,Object> params = ObjectUtil.objectToMap(sqBean);
        PageConvert.currentPageConvertStartIndex(params);
        PageBean pageBean = experimentDataService.getExperimentDataList(params);
        return ResultFactory.buildSuccessResult(pageBean);
    }

    @ApiOperation(value = "更新下机数据文件状态",httpMethod = "POST")
    @PostMapping("/updateExperimentDataStatus")
    public Result updateExperimentDataStatus(@RequestBody int id)  {
        ExperimentData experimentData = new ExperimentData();
        experimentData.setId(id);
        experimentData.setStatus(1+"");
        experimentDataService.updateExperimentData(experimentData);
        return ResultFactory.buildSuccessResult(null);
    }

    public static void main(String[] args) {
        List<ExperimentalDataRow> experimentalDataRowList = new ArrayList<>();
        ExperimentalDataRow dataRow = new ExperimentalDataRow();
        dataRow.setSampleNum("NY123");
        dataRow.setGenotype("");
        dataRow.setSnpId("rs16947");
        dataRow.setDescription("");
        dataRow.setMethodology("CNV");
        experimentalDataRowList.add(dataRow);
        ExperimentalDataRow dataRow2 = new ExperimentalDataRow();
        dataRow2.setSampleNum("NY456");
        dataRow2.setGenotype("");
        dataRow2.setSnpId("rs16949");
        dataRow2.setDescription("");
        dataRow2.setMethodology("CNV");
        experimentalDataRowList.add(dataRow2);
        experimentalDataRowList.add(dataRow2);
        ExperimentalDataRow dataRow3 = new ExperimentalDataRow();
        dataRow3.setSampleNum("NY123");
        dataRow3.setGenotype("");
        dataRow3.setSnpId("rs16950");
        dataRow3.setDescription("");
        dataRow3.setMethodology("CNV");
        experimentalDataRowList.add(dataRow3);

        List<String> cnvErr = new ArrayList<>();
//        cnvErr.add("123");
//        cnvErr.add("123");
//        cnvErr.add("45 6");
//        cnvErr.add("45 6");
//        cnvErr.add("887");
//        experimentalDataRowList.stream().filter(experimentalDataRow -> "CNV".equals(experimentalDataRow.getMethodology()))
//                .collect(Collectors.toList())
//                .stream().map(experimentalDataRow -> {
//            cnvErr.add(experimentalDataRow.getSampleNum());
//            return cnvErr;
//        }).collect(Collectors.toList());
//        List<String> s = cnvErr.stream().distinct().collect(Collectors.toList());

        String[] callResults = ("CT,CT,AG").split(",");
        LinkedHashSet unDumpCall = new LinkedHashSet();
        unDumpCall.addAll(Arrays.asList(callResults));
        for (Object o : unDumpCall) {

            System.out.println(o);
        }

    }



}
