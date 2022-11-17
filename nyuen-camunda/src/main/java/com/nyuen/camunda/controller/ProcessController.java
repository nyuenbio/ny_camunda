package com.nyuen.camunda.controller;

import com.alibaba.fastjson.JSON;
import com.nyuen.camunda.domain.po.ActGeBytearray;
import com.nyuen.camunda.domain.vo.SampleRowAndCell;
import com.nyuen.camunda.domain.vo.SimpleQueryBean;
import com.nyuen.camunda.domain.vo.TodoTask;
import com.nyuen.camunda.mapper.ActGeBytearrayMapper;
import com.nyuen.camunda.result.Result;
import com.nyuen.camunda.result.ResultFactory;
import com.nyuen.camunda.service.MyTaskService;
import com.nyuen.camunda.utils.ExcelUtil;
import com.nyuen.camunda.utils.ObjectUtil;
import com.nyuen.camunda.utils.PageConvert;
import com.nyuen.camunda.domain.vo.AttachmentVo;
import com.nyuen.camunda.domain.vo.DealTaskBean;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

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
    private ProcessEngine processEngine;



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
            return ResultFactory.buildFailResult("当前用户id不能为空");
        }
        if(StringUtils.isEmpty(nodeName)){
            return ResultFactory.buildFailResult("节点名称不能为空");
        }
        // 上传抽提数据,或上传下机数据
        Result result = ExcelUtil.dealExtractionByExcel(multipartFile);
        if(200 != result.getCode()){
            return result;
        }
        List<SampleRowAndCell> sampleRowAndCellList = (List<SampleRowAndCell>) result.getData();
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

    public static void main(String[] args) throws IOException {
//        String s = "2.36";
//        List<String> l1 = new ArrayList<>();
//        l1.add(s);
//        List<List<String>> l2 = new ArrayList<>();
//        l2.add(l1);
//        Object obj = JSON.toJSON(l2);
//        System.out.println("obj          =>   "+obj.toString());
//        String str2 = Base64.getEncoder().encodeToString(obj.toString().getBytes());//"W1siMi4zNiJdXQ==";
        //System.out.println("objToBase64  =>    "+str2);

        String str = "rO0ABXNyAB5jb20uYWxpYmFiYS5mYXN0anNvbi5KU09OQXJyYXkAAAAAAAAAAQIAAUwABGxpc3R0ABBMamF2YS91dGlsL0xpc3Q7eHBzcgATamF2YS51dGlsLkFycmF5TGlzdHiB0h2Zx2GdAwABSQAEc2l6ZXhwAAAAAXcEAAAAAXNxAH4AAHNxAH4AAwAAAAF3BAAAAAF0AAQ2LjY4eHg=";
        System.out.println("str          =>    "+str);
        byte[] bytes = Base64.getDecoder().decode(str.getBytes(StandardCharsets.ISO_8859_1));
        System.out.println("byte[]       =>    "+bytes);
        String decode = new String(bytes, StandardCharsets.ISO_8859_1);

        String f = JSON.toJSONString(decode);
        System.out.println("decode       =>    "+decode);
        //==========================================================================



        //===========================================================================
        InputStream is = new ByteArrayInputStream(bytes);
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int rc;
        while ((rc = is.read(buff, 0, 1024)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        byte[] result = swapStream.toByteArray();
        System.out.println(result);
    }



}
