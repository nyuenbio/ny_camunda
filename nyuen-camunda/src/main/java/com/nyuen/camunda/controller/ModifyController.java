package com.nyuen.camunda.controller;

import com.nyuen.camunda.domain.vo.RejectBean;
import com.nyuen.camunda.result.Result;
import com.nyuen.camunda.result.ResultFactory;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.runtime.ActivityInstance;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * 流程驳回controller
 *
 * @author chengjl
 * @description
 * @date 2022/10/31
 */
@Api(tags = "流程更改控制类")
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
    @ApiOperation(value = "驳回到第一个用户任务节点", httpMethod = "POST")
    @PostMapping("/rejectToFirstNode")
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
        //taskVariable.put("user", assignee);
        taskVariable.put("remark", "流程驳回"+"，驳回原因:" + rejectBean.getRejectComment());
        taskService.createComment(task.getId(), rejectBean.getProcessInstanceId(), "驳回原因:" + rejectBean.getRejectComment());
        taskService.setVariablesLocal(task.getId(),taskVariable);//流程的可变参数赋值
        runtimeService.createProcessInstanceModification(rejectBean.getProcessInstanceId())
                .cancelActivityInstance(getInstanceIdForActivity(tree, task.getTaskDefinitionKey()))//关闭相关任务
                .setAnnotation("进行了驳回到第一个任务节点操作")
                .startBeforeActivity(toActId)//启动目标活动节点
                //.setVariables(taskVariable)//流程的可变参数赋值
                .execute();
        return ResultFactory.buildSuccessResult(null);
    }

    @ApiOperation(value = "驳回到上一个用户任务节点", httpMethod = "POST")
    @PostMapping("/rejectToLastNode")
    public Result rejectToLastNode(@RequestBody RejectBean rejectBean) {
        //获取当前task
        Task task = taskService.createTaskQuery()
                .taskAssignee(rejectBean.getCurrentUserId()) //当前登录用户的id
                .processInstanceId(rejectBean.getProcessInstanceId())
                .singleResult();
        ActivityInstance tree = runtimeService.getActivityInstance(rejectBean.getProcessInstanceId());
        //获取所有已办用户任务节点
        List<HistoricActivityInstance> resultList = historyService
                .createHistoricActivityInstanceQuery()
                .processInstanceId(rejectBean.getProcessInstanceId())
                .activityType("userTask")
                .finished()
                .orderByHistoricActivityInstanceEndTime()
                .asc()
                .list();
        if(null == resultList || resultList.size() == 0){
            return ResultFactory.buildFailResult("当前任务无法驳回！");
        }
        //得到第一个任务节点的id
        HistoricActivityInstance historicActivityInstance = resultList.get(0);
        String startActId = historicActivityInstance.getActivityId();
        if(startActId.equals(task.getTaskDefinitionKey())){
            return ResultFactory.buildFailResult("开始节点无法驳回！");
        }
        //得到上一个任务节点的ActivityId和待办人
        Map<String,String> lastNode = getLastNode(resultList, task.getTaskDefinitionKey());
        if (null == lastNode) {
            return ResultFactory.buildFailResult("回退节点异常！");
        }
        String toActId = lastNode.get("toActId");
        String assignee = lastNode.get("assignee");
        //设置流程中的可变参数
        Map<String, Object> taskVariable = new HashMap<>(2);
//        taskVariable.put("user", assignee);
        taskVariable.put("remark", "流程驳回"+"，驳回原因:" + rejectBean.getRejectComment());
        taskService.createComment(task.getId(), rejectBean.getProcessInstanceId(), "驳回原因:" + rejectBean.getRejectComment());
        taskService.setVariablesLocal(task.getId(),taskVariable);//流程的可变参数赋值
        runtimeService.createProcessInstanceModification(rejectBean.getProcessInstanceId())
                .cancelActivityInstance(getInstanceIdForActivity(tree, task.getTaskDefinitionKey()))//关闭相关任务
                .setAnnotation("进行了驳回到上一个任务节点操作")
                .startBeforeActivity(toActId)//启动目标活动节点
                //.setVariablesLocal(taskVariable)//流程的可变参数赋值
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

    /**
     * 获取上一节点信息
     * 分两种情况：
     * 1、当前节点不在历史节点里
     * 2、当前节点在历史节点里
     * 比如，resultList={1,2,3}
     *     (1)当前节点是4，表示3是完成节点，4驳回需要回退到3
     *     (2)当前节点是2，表示3是驳回节点，3驳回到当前2节点，2驳回需要回退到1
     * 其他驳回过的情况也都包含在情况2中。
     *
     * @param resultList 历史节点列表
     * @param currentActivityId 当前待办节点ActivityId
     * @return 返回值：上一节点的ActivityId和待办人（toActId, assignee）
     */
    private static Map<String,String> getLastNode(List<HistoricActivityInstance> resultList, String currentActivityId){
        Map<String,String> backNode = new HashMap<>();
        //新建一个有序不重复集合
        LinkedHashMap<String,String> linkedHashMap = new LinkedHashMap();
        for(HistoricActivityInstance hai : resultList){
            linkedHashMap.put(hai.getActivityId(),hai.getAssignee());
        }
        //分两种情况：当前节点在不在历史节点里面，当前节点在历史节点里
        //情况1、当前节点不在历史节点里
        int originSize = resultList.size();
        int duplicateRemovalSize = linkedHashMap.size();
        //判断历史节点中是否有重复节点
//        if(originSize == duplicateRemovalSize){
            boolean flag = false;
            for(Map.Entry entry: linkedHashMap.entrySet()){
                if(currentActivityId.equals(entry.getKey())){
                    flag = true;
                    break;
                }
            }
            if(!flag) {
                //当前节点不在历史节点里：最后一个节点是完成节点
                HistoricActivityInstance historicActivityInstance = resultList.get(originSize - 1);
                backNode.put("toActId", historicActivityInstance.getActivityId());
                backNode.put("assignee", historicActivityInstance.getAssignee());
                //backNode.put("toActName",historicActivityInstance.getActivityName());
                return backNode;
            }
//        }
        //情况2、当前节点在历史节点里（已回退过的）
        return currentNodeInHis(linkedHashMap, currentActivityId);
    }

    private static Map<String,String> currentNodeInHis(LinkedHashMap<String,String> linkedHashMap,String currentActivityId){
        //情况2、当前节点在历史节点里（已回退过的）
        Map<String,String> backNode = new HashMap<>();
        ListIterator<Map.Entry<String,String>> li = new ArrayList<>(linkedHashMap.entrySet()).listIterator();
        //System.out.println("已回退过的");
        while (li.hasNext()){
            Map.Entry<String,String> entry = li.next();
            if(currentActivityId.equals(entry.getKey())){
                li.previous();
                Map.Entry<String,String> previousEntry = li.previous();
                backNode.put("toActId",previousEntry.getKey());
                backNode.put("assignee",previousEntry.getValue());
                return backNode;
            }
        }
        return null;
    }

    //@ApiOperation(value = "(任务发起人)撤回用户任务", httpMethod = "GET")
    @GetMapping("/withdraw")
    public Result withdraw(String processId,String userId){
        // 1、验证：当前流程实例状态；当前执行人为发起人
        // 2、取消：取消产生的任务，查找并取消
        // 3、删除此流程实例



        return ResultFactory.buildSuccessResult(null);
    }

    @ApiOperation(value = "撤销用户任务(未测试)", httpMethod = "DELETE")
    @DeleteMapping("/cancelTask")
    public Result cancelTask(String procId,@ApiParam("当前用户id") String userId){
        // 撤销用户任务 todo
        // 1、验证 流程任务状态，当前执行人为发起人
        if(!checkProcessStarter(procId,userId)){
            return ResultFactory.buildFailResult("撤回失败：当前执行人不是流程发起人，请核实！");
        }
        if(!checkProcessInstanceState(procId)){
            return ResultFactory.buildFailResult("撤回失败：流程并未在流转中或流程已结束，请核实！");
        }
        List<Task> taskList = taskService.createTaskQuery().processInstanceId(procId).list();
        if(CollectionUtils.isEmpty(taskList)){
            return ResultFactory.buildFailResult("撤回失败：该流程上无任务流转，请核实！");
        }
        Task task = taskList.get(0);
        List<HistoricActivityInstance> historicActivityInstanceList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(task.getProcessInstanceId())
                .activityType("userTask")
                .finished().orderByHistoricActivityInstanceEndTime()
                .asc().list();
        if(historicActivityInstanceList == null || historicActivityInstanceList.isEmpty()){
            return ResultFactory.buildFailResult("撤回失败：该流程上无用户任务，请核实！");
        }
        // 2、取消 取消产生的任务，查找此流程产生的task并取消
        ActivityInstance activityInstance = runtimeService.getActivityInstance(task.getProcessInstanceId());
        String toActId = historicActivityInstanceList.get(0).getActivityId();
        String assignee = historicActivityInstanceList.get(0).getAssignee();
        Map<String, Object> taskVariable = new HashMap<>();
        //设置当前处理人
        taskVariable.put("assignee", assignee);
        runtimeService.createProcessInstanceModification(task.getProcessInstanceId())
                //关闭相关任务
                .cancelActivityInstance(getInstanceIdForActivity(activityInstance, task.getTaskDefinitionKey()))
                .setAnnotation("进行了撤回到节点操作")
                //启动目标活动节点
                .startBeforeActivity(toActId)
                //流程的可变参数赋值
                .setVariables(taskVariable)
                .execute();
        // 3、删除此流程实例
        runtimeService.deleteProcessInstance(task.getProcessInstanceId(), String.format("%s 用户执行了撤回操作", userId));
        return ResultFactory.buildResult(200,"撤回成功", null);
    }

    private boolean checkProcessStarter(String procId, String userId){
        List<HistoricProcessInstance> hpiList =  historyService.createHistoricProcessInstanceQuery()
                .processInstanceId(procId)
                .startedBy(userId)
                .list();
        return null != hpiList && hpiList.size() > 0;
    }
    private boolean checkProcessInstanceState(String procId){
        ProcessInstance processInstance = runtimeService.createProcessInstanceQuery()
                .processInstanceId(procId)
                .singleResult();
        if(ObjectUtils.isEmpty(processInstance)){
            return false;//5 此流程并未在流转中
        }
        if(processInstance.isEnded()) {
            return false;//6 此流程已结束
        }
        return true;
    }

    public static void main(String[] args) {
        HistoricActivityInstance hai1 = new HistoricActivityInstance() {
            @Override
            public String getId() {
                return "act-1";
            }

            @Override
            public String getParentActivityInstanceId() {
                return null;
            }

            @Override
            public String getActivityId() {
                return "act-1";
            }

            @Override
            public String getActivityName() {
                return null;
            }

            @Override
            public String getActivityType() {
                return null;
            }

            @Override
            public String getProcessDefinitionKey() {
                return null;
            }

            @Override
            public String getProcessDefinitionId() {
                return null;
            }

            @Override
            public String getRootProcessInstanceId() {
                return null;
            }

            @Override
            public String getProcessInstanceId() {
                return null;
            }

            @Override
            public String getExecutionId() {
                return null;
            }

            @Override
            public String getTaskId() {
                return null;
            }

            @Override
            public String getCalledProcessInstanceId() {
                return null;
            }

            @Override
            public String getCalledCaseInstanceId() {
                return null;
            }

            @Override
            public String getAssignee() {
                return "user-1";
            }

            @Override
            public Date getStartTime() {
                return null;
            }

            @Override
            public Date getEndTime() {
                return null;
            }

            @Override
            public Long getDurationInMillis() {
                return null;
            }

            @Override
            public boolean isCompleteScope() {
                return false;
            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public String getTenantId() {
                return null;
            }

            @Override
            public Date getRemovalTime() {
                return null;
            }
        };
        HistoricActivityInstance hai2 = new HistoricActivityInstance() {
            @Override
            public String getId() {
                return "act-2";
            }

            @Override
            public String getParentActivityInstanceId() {
                return null;
            }

            @Override
            public String getActivityId() {
                return "act-2";
            }

            @Override
            public String getActivityName() {
                return null;
            }

            @Override
            public String getActivityType() {
                return null;
            }

            @Override
            public String getProcessDefinitionKey() {
                return null;
            }

            @Override
            public String getProcessDefinitionId() {
                return null;
            }

            @Override
            public String getRootProcessInstanceId() {
                return null;
            }

            @Override
            public String getProcessInstanceId() {
                return null;
            }

            @Override
            public String getExecutionId() {
                return null;
            }

            @Override
            public String getTaskId() {
                return null;
            }

            @Override
            public String getCalledProcessInstanceId() {
                return null;
            }

            @Override
            public String getCalledCaseInstanceId() {
                return null;
            }

            @Override
            public String getAssignee() {
                return "user-2";
            }

            @Override
            public Date getStartTime() {
                return null;
            }

            @Override
            public Date getEndTime() {
                return null;
            }

            @Override
            public Long getDurationInMillis() {
                return null;
            }

            @Override
            public boolean isCompleteScope() {
                return false;
            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public String getTenantId() {
                return null;
            }

            @Override
            public Date getRemovalTime() {
                return null;
            }
        };
        HistoricActivityInstance hai3 = new HistoricActivityInstance() {
            @Override
            public String getId() {
                return "act-3";
            }

            @Override
            public String getParentActivityInstanceId() {
                return null;
            }

            @Override
            public String getActivityId() {
                return "act-3";
            }

            @Override
            public String getActivityName() {
                return null;
            }

            @Override
            public String getActivityType() {
                return null;
            }

            @Override
            public String getProcessDefinitionKey() {
                return null;
            }

            @Override
            public String getProcessDefinitionId() {
                return null;
            }

            @Override
            public String getRootProcessInstanceId() {
                return null;
            }

            @Override
            public String getProcessInstanceId() {
                return null;
            }

            @Override
            public String getExecutionId() {
                return null;
            }

            @Override
            public String getTaskId() {
                return null;
            }

            @Override
            public String getCalledProcessInstanceId() {
                return null;
            }

            @Override
            public String getCalledCaseInstanceId() {
                return null;
            }

            @Override
            public String getAssignee() {
                return "user-3";
            }

            @Override
            public Date getStartTime() {
                return null;
            }

            @Override
            public Date getEndTime() {
                return null;
            }

            @Override
            public Long getDurationInMillis() {
                return null;
            }

            @Override
            public boolean isCompleteScope() {
                return false;
            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public String getTenantId() {
                return null;
            }

            @Override
            public Date getRemovalTime() {
                return null;
            }
        };
        HistoricActivityInstance hai4 = new HistoricActivityInstance() {
            @Override
            public String getId() {
                return "act-4";
            }

            @Override
            public String getParentActivityInstanceId() {
                return null;
            }

            @Override
            public String getActivityId() {
                return "act-4";
            }

            @Override
            public String getActivityName() {
                return null;
            }

            @Override
            public String getActivityType() {
                return null;
            }

            @Override
            public String getProcessDefinitionKey() {
                return null;
            }

            @Override
            public String getProcessDefinitionId() {
                return null;
            }

            @Override
            public String getRootProcessInstanceId() {
                return null;
            }

            @Override
            public String getProcessInstanceId() {
                return null;
            }

            @Override
            public String getExecutionId() {
                return null;
            }

            @Override
            public String getTaskId() {
                return null;
            }

            @Override
            public String getCalledProcessInstanceId() {
                return null;
            }

            @Override
            public String getCalledCaseInstanceId() {
                return null;
            }

            @Override
            public String getAssignee() {
                return "user-4";
            }

            @Override
            public Date getStartTime() {
                return null;
            }

            @Override
            public Date getEndTime() {
                return null;
            }

            @Override
            public Long getDurationInMillis() {
                return null;
            }

            @Override
            public boolean isCompleteScope() {
                return false;
            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public String getTenantId() {
                return null;
            }

            @Override
            public Date getRemovalTime() {
                return null;
            }
        };
        HistoricActivityInstance hai5 = new HistoricActivityInstance() {
            @Override
            public String getId() {
                return "act-5";
            }

            @Override
            public String getParentActivityInstanceId() {
                return null;
            }

            @Override
            public String getActivityId() {
                return "act-5";
            }

            @Override
            public String getActivityName() {
                return null;
            }

            @Override
            public String getActivityType() {
                return null;
            }

            @Override
            public String getProcessDefinitionKey() {
                return null;
            }

            @Override
            public String getProcessDefinitionId() {
                return null;
            }

            @Override
            public String getRootProcessInstanceId() {
                return null;
            }

            @Override
            public String getProcessInstanceId() {
                return null;
            }

            @Override
            public String getExecutionId() {
                return null;
            }

            @Override
            public String getTaskId() {
                return null;
            }

            @Override
            public String getCalledProcessInstanceId() {
                return null;
            }

            @Override
            public String getCalledCaseInstanceId() {
                return null;
            }

            @Override
            public String getAssignee() {
                return "user-5";
            }

            @Override
            public Date getStartTime() {
                return null;
            }

            @Override
            public Date getEndTime() {
                return null;
            }

            @Override
            public Long getDurationInMillis() {
                return null;
            }

            @Override
            public boolean isCompleteScope() {
                return false;
            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public String getTenantId() {
                return null;
            }

            @Override
            public Date getRemovalTime() {
                return null;
            }
        };
        List<HistoricActivityInstance> resultList = new ArrayList<>();
        resultList.add(hai1);
        resultList.add(hai2);
        resultList.add(hai3);
        resultList.add(hai4);
        resultList.add(hai3);
        resultList.add(hai4);
        resultList.add(hai5);
        Map<String,String> map = getLastNode(resultList,"act-6");
        System.out.println("toActId ==> "+map.get("toActId"));
        System.out.println("assignee ==> "+map.get("assignee"));

    }


}
