package com.nyuen.camunda.controller;

import com.nyuen.camunda.domain.vo.RejectBean;
import com.nyuen.camunda.result.Result;
import com.nyuen.camunda.result.ResultFactory;
import io.swagger.annotations.ApiOperation;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程驳回controller
 *
 * @author chengjl
 * @description
 * @date 2022/10/31
 */
@RestController
@RequestMapping("/modify")
public class ModifyController {
    @Resource
    private TaskService taskService;
    @Resource
    private HistoryService historyService;
    @Resource
    private RuntimeService runtimeService;

    /**
     * camunda驳回到第一个任务节点
     */
    @ApiOperation(value = "驳回到第一个用户任务节点", httpMethod = "GET")
    @GetMapping("/rejectToFirstNode")
    public Result rejectToFirstNode(@RequestBody RejectBean rejectBean) {
        //String rejectMessage="项目的金额款项结算不正确";
        Task task = taskService.createTaskQuery()
                .taskAssignee(rejectBean.getCurrentUserId()) //当前登录用户的id
                .processInstanceId(rejectBean.getProcessInstanceId())
                .singleResult();
        ActivityInstance tree = runtimeService.getActivityInstance(rejectBean.getProcessInstanceId());
        List<HistoricActivityInstance> resultList = historyService
                .createHistoricActivityInstanceQuery()
                .processInstanceId(rejectBean.getProcessInstanceId())
                .activityType("userTask")
                .finished()
                .orderByHistoricActivityInstanceEndTime()
                .asc()
                .list();
        if(null == resultList || resultList.size()<2){
            return ResultFactory.buildFailResult("第一个用户节点无法驳回！");
        }
        //得到第一个任务节点的id
        HistoricActivityInstance historicActivityInstance = resultList.get(0);
        String toActId = historicActivityInstance.getActivityId();
        String assignee = historicActivityInstance.getAssignee();
        //设置流程中的可变参数
        Map<String, Object> taskVariable = new HashMap<>(2);
        taskVariable.put("user", assignee);
        //taskVariable.put("formName", "流程驳回");
        taskService.createComment(task.getId(), rejectBean.getProcessInstanceId(), "驳回原因:" + rejectBean.getRejectComment());
        runtimeService.createProcessInstanceModification(rejectBean.getProcessInstanceId())
                .cancelActivityInstance(getInstanceIdForActivity(tree, task.getTaskDefinitionKey()))//关闭相关任务
                .setAnnotation("进行了驳回到第一个任务节点操作")
                .startBeforeActivity(toActId)//启动目标活动节点
                .setVariables(taskVariable)//流程的可变参数赋值
                .execute();
        return ResultFactory.buildSuccessResult(null);
    }

    @ApiOperation(value = "驳回到上一个用户任务节点", httpMethod = "GET")
    @GetMapping("/rejectToLastNode")
    public Result rejectToLastNode(@RequestBody RejectBean rejectBean) {
        Task task = taskService.createTaskQuery()
                .taskAssignee(rejectBean.getCurrentUserId()) //当前登录用户的id
                .processInstanceId(rejectBean.getProcessInstanceId())
                .singleResult();
        ActivityInstance tree = runtimeService.getActivityInstance(rejectBean.getProcessInstanceId());
        List<HistoricActivityInstance> resultList = historyService
                .createHistoricActivityInstanceQuery()
                .processInstanceId(rejectBean.getProcessInstanceId())
                .activityType("userTask")
                .finished()
                .orderByHistoricActivityInstanceEndTime()
                .asc()
                .list();
        if(null == resultList || resultList.size()<2){
            return ResultFactory.buildFailResult("第一个用户节点无法驳回！");
        }
        //得到上一个任务节点的id
        HistoricActivityInstance historicActivityInstance = resultList.get(resultList.size()-1);
        String toActId = historicActivityInstance.getActivityId();
        String assignee = historicActivityInstance.getAssignee();
        //设置流程中的可变参数
        Map<String, Object> taskVariable = new HashMap<>(2);
        taskVariable.put("user", assignee);
        //taskVariable.put("formName", "流程驳回");
        taskService.createComment(task.getId(), rejectBean.getProcessInstanceId(), "驳回原因:" + rejectBean.getRejectComment());
        runtimeService.createProcessInstanceModification(rejectBean.getProcessInstanceId())
                .cancelActivityInstance(getInstanceIdForActivity(tree, task.getTaskDefinitionKey()))//关闭相关任务
                .setAnnotation("进行了驳回到上一个任务节点操作")
                .startBeforeActivity(toActId)//启动目标活动节点
                .setVariables(taskVariable)//流程的可变参数赋值
                .execute();
        return ResultFactory.buildSuccessResult(null);
    }

    private String getInstanceIdForActivity(ActivityInstance activityInstance, String activityId) {
        ActivityInstance instance = getChildInstanceForActivity(activityInstance, activityId);
        if (instance != null) {
            return instance.getId();
        }
        return null;
    }

    private ActivityInstance getChildInstanceForActivity(ActivityInstance activityInstance, String activityId) {
        if (activityId.equals(activityInstance.getActivityId())) {
            return activityInstance;
        }
        for (ActivityInstance childInstance : activityInstance.getChildActivityInstances()) {
            ActivityInstance instance = getChildInstanceForActivity(childInstance, activityId);
            if (instance != null) {
                return instance;
            }
        }
        return null;
    }


}
