package com.nyuen.camunda.controller;

import com.alibaba.fastjson.JSON;
import com.nyuen.camunda.common.PageBean;
import com.nyuen.camunda.domain.po.ActGeBytearray;
import com.nyuen.camunda.domain.po.ExperimentData;
import com.nyuen.camunda.domain.vo.*;
import com.nyuen.camunda.mapper.ActGeBytearrayMapper;
import com.nyuen.camunda.result.Result;
import com.nyuen.camunda.result.ResultFactory;
import com.nyuen.camunda.service.ExperimentDataService;
import com.nyuen.camunda.service.MyTaskService;
import com.nyuen.camunda.utils.ExcelUtil;
import com.nyuen.camunda.utils.FileUtil;
import com.nyuen.camunda.utils.ObjectUtil;
import com.nyuen.camunda.utils.PageConvert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Attachment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
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
    private RepositoryService repositoryService;
    @Resource
    private ActGeBytearrayMapper geBytearrayMapper;
    @Resource
    private ExperimentDataService experimentDataService;

    @Value("${file.saveRootPath}")
    private String saveRootPath;
    @Value("${file.readRootPath}")
    private String readRootPath;



    @ApiOperation(value = "根据样本编号查询流程",httpMethod = "POST")
    @PostMapping("/getSampleProcessList")
    public Result getTodoList(@RequestBody SimpleQueryBean sqBean) throws IllegalAccessException {
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
                                       String procDefId,@ApiParam("节点名称") String nodeName)
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
        // 1、校验表格 V3版本规则
        Result checkResult = experimentDataCheckV3(sampleRowAndCellList, nodeName);
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
        experimentDataService.addExperimentData(experimentData);
        //}
        // 3、处理流程节点
        return dealTaskNodeByExcel(sampleRowAndCellList,assignee,procDefId,nodeName);
    }

    private Result experimentDataCheckV3(List<SampleRowAndCell> sampleRowAndCellList,String nodeName){
        if(!StringUtils.equalsIgnoreCase(nodeName.trim(),"下机")){
            return ResultFactory.buildSuccessResult(null);
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
            if(todoTaskList.size() == 0){
                sb.append(sampleRowAndCell.getSampleInfo()).append(" , ");
            }else if(todoTaskList.size() > 1){
                return ResultFactory.buildFailResult("样本 "+sampleRowAndCell.getSampleInfo()+" 存在两条待办流程，无法匹配！");
            }else {
                String taskId = todoTaskList.get(0).getId();
                // 添加流程变量
                Map<String, Object> variables = new HashMap<>();
                //variables.put("节点名称", nodeName);
                //variables.put("样本编号", sampleRowAndCell.getSampleInfo());
                variables.put(nodeName+"表格数据", JSON.toJSON(sampleRowAndCell.getSampleRowList()));
                taskService.setVariablesLocal(taskId, variables);
                taskService.complete(taskId);
            }
        }
        return ResultFactory.buildResult(200,"".equals(sb.toString().trim()) ? "全部处理完成" : sb.toString()+"以上样本匹配失败,原因：您的待办节点中无此样本",null);
    }

    @GetMapping("/testData")
    public byte[] testData(String  id) throws IOException {
        ActGeBytearray actGeBytearray = geBytearrayMapper.selectByPrimaryKey(id);
        InputStream is = new ByteArrayInputStream(actGeBytearray.getBytes());
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int rc;
        while ((rc = is.read(buff, 0, 1024)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        return swapStream.toByteArray();
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
        experimentalDataRowList.stream().filter(experimentalDataRow -> "CNV".equals(experimentalDataRow.getMethodology()))
                .collect(Collectors.toList())
                .stream().map(experimentalDataRow -> {
            cnvErr.add(experimentalDataRow.getSampleNum());
            return cnvErr;
        }).collect(Collectors.toList());
        List<String> s = cnvErr.stream().distinct().collect(Collectors.toList());
        System.out.println(s.toString());



    }



}
