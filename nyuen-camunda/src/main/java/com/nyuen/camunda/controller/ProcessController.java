package com.nyuen.camunda.controller;

import com.nyuen.camunda.domain.vo.SimpleQueryBean;
import com.nyuen.camunda.result.Result;
import com.nyuen.camunda.result.ResultFactory;
import com.nyuen.camunda.service.MyTaskService;
import com.nyuen.camunda.utils.ObjectUtil;
import com.nyuen.camunda.utils.PageConvert;
import com.nyuen.camunda.vo.AttachmentVo;
import com.nyuen.camunda.vo.DealTaskBean;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.IdentityService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.task.Attachment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
