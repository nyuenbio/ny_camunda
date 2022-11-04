package com.nyuen.camunda.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.nyuen.camunda.domain.vo.SampleRowAndCell;
import com.nyuen.camunda.domain.vo.SimpleQueryBean;
import com.nyuen.camunda.domain.vo.TodoTask;
import com.nyuen.camunda.result.Result;
import com.nyuen.camunda.result.ResultFactory;
import com.nyuen.camunda.service.MyTaskService;
import com.nyuen.camunda.utils.ExcelUtil;
import com.nyuen.camunda.utils.ObjectUtil;
import com.nyuen.camunda.utils.PageConvert;
import com.nyuen.camunda.vo.AttachmentVo;
import com.nyuen.camunda.vo.DealTaskBean;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Attachment;
import org.camunda.commons.utils.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2022/10/24
 */
@RestController
@RequestMapping("/process")
public class ProcessController {
    @Resource
    private MyTaskService myTaskService;
    @Resource
    private TaskService taskService;
    @Resource
    private IdentityService identityService;



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

    //通过上传解析Excel表格，处理流程节点并添加流程变量
    @PostMapping("/dealExtractionByExcel")
    public Result dealExtractionByExcel(MultipartFile multipartFile, String nodeName, String assignee,String procDefId)
            throws IOException, InvalidFormatException {
        if(StringUtils.isEmpty(assignee)){
            return ResultFactory.buildFailResult("当前用户id不能为空");
        }
        // 上传抽提数据,或上传下机数据
        //添加流程变量
        // todo
        Result result = ExcelUtil.dealExtractionByExcel(multipartFile);
        if(200 != result.getCode()){
            return result;
        }
        List<SampleRowAndCell> sampleRowAndCellList = (List<SampleRowAndCell>) result.getData();
        StringBuilder sb = null;
        for(SampleRowAndCell sampleRowAndCell : sampleRowAndCellList){
            //添加审批人
            if(StringUtils.isNotEmpty(assignee)) {
                identityService.setAuthenticatedUserId(assignee);
            }
            // 存储流程变量
            Map<String,Object> variables = new HashMap<>();
            variables.put("节点名称", nodeName == null ?"":nodeName);
            variables.put("样本编号", sampleRowAndCell.getSampleInfo());
            variables.put("数据", JSON.toJSON(sampleRowAndCell.getSampleRowList()));
            Map<String,Object> params = new HashMap<>();
            params.put("assignee",assignee);
            params.put("sampleInfo", sampleRowAndCell.getSampleInfo());
            params.put("procDefId",procDefId);
            List<TodoTask> todoTaskList = myTaskService.getTodoTaskByCondition(params);
            if(todoTaskList.size() == 0){
                sb.append(sampleRowAndCell.getSampleInfo()).append(" , ");
            }
            if(todoTaskList.size() > 1){
                return ResultFactory.buildFailResult("样本 "+sampleRowAndCell.getSampleInfo()+" 存在两条待办流程，无法匹配！");
            }
            String taskId = todoTaskList.get(0).getId();
            // 根据样本编号（businessKey)和assignee找到taskId
            taskService.setVariables(taskId,variables);
            taskService.complete(taskId);
        }
        return ResultFactory.buildSuccessResult(sb == null ? null : sb.toString()+"以上样本匹配失败,原因：您的待办中无此样本");
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


}
