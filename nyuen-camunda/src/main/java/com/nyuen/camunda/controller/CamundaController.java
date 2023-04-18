package com.nyuen.camunda.controller;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.nyuen.camunda.common.PageBean;
import com.nyuen.camunda.domain.po.ActIdGroup;
import com.nyuen.camunda.domain.po.ActIdUser;
import com.nyuen.camunda.domain.vo.*;
import com.nyuen.camunda.result.Result;
import com.nyuen.camunda.result.ResultFactory;
import com.nyuen.camunda.service.MyIdentityService;
import com.nyuen.camunda.service.MyTaskService;
import com.nyuen.camunda.utils.CamundaObjectUtil;
import com.nyuen.camunda.utils.ObjectUtil;
import com.nyuen.camunda.utils.PageConvert;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.history.*;
import org.camunda.bpm.engine.identity.*;
import org.camunda.bpm.engine.impl.persistence.entity.GroupEntity;
import org.camunda.bpm.engine.impl.persistence.entity.TenantEntity;
import org.camunda.bpm.engine.impl.persistence.entity.UserEntity;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.task.Task;
import org.camunda.commons.utils.IoUtil;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;


import javax.annotation.Resource;
import java.io.*;
import java.util.*;


/**
 * Demo Controller
 *
 * @author chengjl
 * @description Demo Controller
 * @date 2022/7/26
 */
@Api(tags = "主要操作控制类")
@RestController
@RequestMapping("/ny")
public class CamundaController {
    @Resource
    private ProcessEngine processEngine;
    @Resource
    private RepositoryService repositoryService;
    @Resource
    private TaskService taskService;
    @Resource
    private HistoryService historyService;
    @Resource
    private RuntimeService runtimeService;
    @Resource
    private IdentityService identityService;
    @Resource
    private FormService formService;
    @Resource
    private MyTaskService myTaskService;
    @Resource
    private MyIdentityService myIdentityService;

    //0、获取所有流程部署信息
    @ApiIgnore
    @ApiOperation(value = "获取所有流程部署信息",httpMethod = "GET")
    @GetMapping("/getDeployedProcessList")
    public String getDeployedProcessList(){
        List<Deployment> list=repositoryService.createDeploymentQuery()
                .orderByDeploymentTime()
                .desc()
                .list();

        return JSONArray.toJSONString(list,SerializerFeature.IgnoreErrorGetter);
    }

    //获取所有已部署流程定义
    @ApiOperation(value = "获取所有已部署流程定义")
    @GetMapping("/getProcessDefinitionList")
    public Result getProcessDefinitionList(){
        List<ProcessDefinition> list=repositoryService.createProcessDefinitionQuery()
                .orderByDeploymentTime()
                .desc()
                .list();

        return ResultFactory.buildSuccessResult(JSONArray.toJSONString(list,SerializerFeature.IgnoreErrorGetter));
        //return JSONArray.toJSONString(list,SerializerFeature.IgnoreErrorGetter);
    }

    //1、部署流程图-字符串方式部署
    @ApiOperation(value = "部署表单)")
    @PostMapping("/deployFormByText")
    public Result deployFormByText(@RequestBody DeploymentBean deploymentBean){
        Deployment deployment = processEngine.getRepositoryService()//获取流程定义和部署对象相关的Service
                .createDeployment()//创建部署对象
                .name(deploymentBean.getName()+"_form")//eg: 表单一号_form
                .source(deploymentBean.getSource())
                .addString(deploymentBean.getName()+"_form.form",deploymentBean.getText())
                .deploy();//完成部署
        return ResultFactory.buildSuccessResult("表单"+deployment.getName()+"-->"+deployment.getSource()+"部署成功");
    }
    @ApiOperation(value = "部署流程图-字符串方式部署(无或多个表单部署)")
    @PostMapping("/deployMultiFormProcessByText")
    public Result deployMultiFormProcessByText(@RequestBody DeploymentBean deploymentBean){
        DeploymentBuilder dpb = repositoryService.createDeployment()
                .name(deploymentBean.getName())//eg: 第一个demo1流程
                .source(deploymentBean.getSource())
                .addString(deploymentBean.getName()+".bpmn",deploymentBean.getText());
        if(null != deploymentBean.getFormBeanList() && deploymentBean.getFormBeanList().size()>0) {
            for (FormBean fb : deploymentBean.getFormBeanList()) {
                dpb.addString(fb.getFormId() + ".form", JSONObject.toJSONString(fb.getFormText()));
            }
        }
        Deployment deployment = dpb.deploy();//完成部署
        return ResultFactory.buildSuccessResult(deployment.getName()+"-->"+deployment.getSource()+"部署成功");
    }

    @ApiOperation(value = "根据taskId获取form内容",httpMethod = "GET")
    @GetMapping("/getFormByTaskId")
    public byte[] getFormByTaskId(@RequestParam("taskId") String taskId) throws IOException {
        // todo 有bug???没有
        InputStream is = formService.getDeployedTaskForm(taskId);

        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int rc;
        while ((rc = is.read(buff, 0, 1024)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        return swapStream.toByteArray();
    }
    @ApiOperation(value = "getTaskFormData",httpMethod = "GET")
    @GetMapping("/getTaskFormData")
    public String getTaskFormData(@RequestParam("taskId") String taskId) {
        //不适用于已办节点
        //Object obj = formService.getRenderedTaskForm(taskId);
        //不适用于已办节点
        TaskFormData tfData = formService.getTaskFormData(taskId);
        return JSONArray.toJSONString(tfData,SerializerFeature.IgnoreErrorGetter);
    }

    @ApiOperation(value = "根据processInstanceId获取流程变量",httpMethod = "GET")
    @GetMapping("/getProcessVariable")
    public Result getProcessVariable(@RequestParam("procInstId") String procInstId) {
        //不适用于已办节点
        //Object obj = formService.getRenderedTaskForm(taskId);
        //不适用于已办节点
        //TaskFormData tfData = formService.getTaskFormData(taskId);
        // 不适用于待办节点,待办节点本身就还没有变量
        List<HistoricVariableInstance> hviList = historyService.createHistoricVariableInstanceQuery()
                .processInstanceIdIn(procInstId)
                .activityInstanceIdIn(procInstId)
                .list();
        //return ResultFactory.buildSuccessResult(CamundaObjectUtil.objectListToJSONArray(hviList));
        List<MyVariable> result = new ArrayList<>();
        for(HistoricVariableInstance h: hviList){
            MyVariable myVariable = new MyVariable(h.getName(),h.getVariableTypeName(),h.getTypedValue().getValue());
            result.add(myVariable);
        }
        return ResultFactory.buildSuccessResult(result);
    }
    @ApiOperation(value = "根据taskId获取流程变量",httpMethod = "GET")
    @GetMapping("/getProcessVariableByTaskId")
    public Result getProcessVariableByTaskId(@RequestParam("taskId") String taskId) {
        // 不适用于待办节点,待办节点本身就还没有变量
        List<HistoricVariableInstance> hviList = historyService.createHistoricVariableInstanceQuery()
                .taskIdIn(taskId)
                .list();
        List<MyVariable> result = new ArrayList<>();
        for(HistoricVariableInstance h: hviList){
            MyVariable myVariable = new MyVariable(h.getName(),h.getVariableTypeName(),h.getTypedValue().getValue());
            result.add(myVariable);
        }
        return ResultFactory.buildSuccessResult(result);
    }

    /**
     * 获取当前task
     *
     */
    /*@GetMapping(value = "task/active/{processId}")
    public TaskVO tasks(@PathVariable(value = "processId") String processId) {
        Task task = taskService.createTaskQuery().processInstanceId(processId).active().singleResult();
        if (Objects.isNull(task)) {
            return null;
        }
        List<TaskFormFieldVO> formFields = new ArrayList<>();
        TaskFormData formData = formService.getTaskFormData(task.getId());
        if (Objects.nonNull(formData) && !CollectionUtils.isEmpty(formData.getFormFields())) {
            formFields = formData.getFormFields().stream().map(t -> TaskFormFieldVO.builder().businessKey(t.isBusinessKey()).id(t.getId()).label(t.getLabel()).type(t.getTypeName()).build()).collect(Collectors.toList());
        }
        return TaskVO.builder().id(task.getId()).name(task.getName()).description(task.getDescription()).formFields(formFields).build();
    }*/

    @ApiOperation(value = "获取所有已部署的form列表",httpMethod = "GET")
    @GetMapping("/getDeployFormList")
    public Result getDeployFormList() {
        List<Deployment> deployFormList=repositoryService.createDeploymentQuery()
                .deploymentNameLike("%\\_form")
                .orderByDeploymentTime()
                .desc()
                .list();

        return ResultFactory.buildSuccessResult(JSONArray.toJSONString(deployFormList, SerializerFeature.IgnoreErrorGetter));
    }
    @ApiOperation(value = "获取已部署的form内容",httpMethod = "GET")
    @GetMapping("/getDeployForm")
    public byte[] getDeployForm(String deploymentId,String name) throws IOException {
        //List<String> deployFormList=repositoryService.getDeploymentResourceNames(deploymentId);
        InputStream is = repositoryService.getResourceAsStream(deploymentId,name+".form");
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int rc;
        while ((rc = is.read(buff, 0, 1024)) > 0) {
            swapStream.write(buff, 0, rc);
        }

        return swapStream.toByteArray();
    }
    @GetMapping("/getDeployForm2")
    public Object getDeployForm2(String deploymentId,String name) throws IOException {
        //List<String> deployFormList=repositoryService.getDeploymentResourceNames(deploymentId);
        InputStream is;
        try {
            is = repositoryService.getResourceAsStream(deploymentId,name+".form");
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int rc;
        while ((rc = is.read(buff, 0, 1024)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        return swapStream.toByteArray();
    }


    @ApiOperation(value = "删除定义（流程或表单），" +
            "如果启用该值，cascade为true，则与此定义相关的所有实例（包括历史实例）也将被删除。"
            ,httpMethod = "DELETE")
    @DeleteMapping("/delProcessDefinition")
    public Result delProcessDefinition(@RequestParam("deploymentId") String deploymentId,@RequestParam("cascade") boolean cascade){
        repositoryService.deleteProcessDefinition(deploymentId,cascade);
        return ResultFactory.buildSuccessResult(null);
    }
    @ApiOperation(value = "删除部署（流程或表单）"+
            "如果启用该值，cascade为true，则与此部署相关的所有实例（包括历史实例）也将被删除。"
            ,httpMethod = "DELETE")
    @DeleteMapping("/delDeployment")
    public Result delDeployment(@RequestParam("deploymentId") String deploymentId,@RequestParam("cascade") boolean cascade){
        repositoryService.deleteDeployment(deploymentId,cascade);
        return ResultFactory.buildSuccessResult(null);
    }


    //2、开启流程
    @ApiOperation(value = "开启流程",httpMethod = "POST")
    @PostMapping("/startProcess")
    public Result startProcess(@RequestBody StartProcessBean startProcessBean){
        // todo 如果是臻慧选产品，处理套餐与孔位规则
        identityService.setAuthenticatedUserId(startProcessBean.getInitiator());
        runtimeService.startProcessInstanceById(startProcessBean.getProcDefId(),
                startProcessBean.getBusinessKey(),startProcessBean.getVariables());

        return ResultFactory.buildSuccessResult(null);
    }

    @ApiOperation(value = "批量开启流程",httpMethod = "POST")
    @PostMapping("/batchStartProcess")
    public Result batchStartProcess(@RequestBody BatchStartProcessBean batchStartProcessBean){
        String initiator = batchStartProcessBean.getInitiator();
        String procDefId = batchStartProcessBean.getProcDefId();
        Map<String,Object> variables = batchStartProcessBean.getVariables();
        for(String businessKey : batchStartProcessBean.getBusinessKeyList()) {
            identityService.setAuthenticatedUserId(initiator);
            runtimeService.startProcessInstanceById(procDefId, businessKey, variables);
        }
        return ResultFactory.buildSuccessResult(null);
    }

    //3、处理流程节点
    @ApiOperation(value = "处理流程节点(带全局变量的节点)",httpMethod = "POST")
    @PostMapping("/dealTask")
    public Result dealTask(@RequestBody DealTaskBean dealTaskBean){
        if(null == dealTaskBean || null == dealTaskBean.getTaskId()){
            return ResultFactory.buildFailResult("taskId不能为空");
        }
        //添加审批人
        if(StringUtils.isNotEmpty(dealTaskBean.getAssignee())) {
            processEngine.getIdentityService().setAuthenticatedUserId(dealTaskBean.getAssignee());
        }
        //添加审批意见，可在Act_Hi_Comment里的message查询到
        //三个参数分别为待办任务ID,流程实例ID,审批意见
        if(StringUtils.isNotEmpty(dealTaskBean.getComment())) {
            taskService.createComment(dealTaskBean.getTaskId(), dealTaskBean.getProcInstId(), dealTaskBean.getComment());
        }
        //处理节点变量
        if(null != dealTaskBean.getVariables()) {
            taskService.setVariables(dealTaskBean.getTaskId(), dealTaskBean.getVariables());
        }
        //处理节点表单 TODO
        //formService.submitTaskForm(dealTaskBean.getTaskId(), dealTaskBean.getVariables());
        //任务完成,也就是审批通过
        taskService.complete(dealTaskBean.getTaskId());

        return ResultFactory.buildSuccessResult(null);
    }
    @ApiOperation(value = "处理流程节点(带局部变量的节点)",httpMethod = "POST")
    @PostMapping("/dealTaskLocal")
    public Result dealTaskLocal(@RequestBody DealTaskBean dealTaskBean){
        if(null == dealTaskBean || null == dealTaskBean.getTaskId()){
            return ResultFactory.buildFailResult("taskId不能为空");
        }
        //添加审批人
        if(StringUtils.isNotEmpty(dealTaskBean.getAssignee())) {
            processEngine.getIdentityService().setAuthenticatedUserId(dealTaskBean.getAssignee());
        }
        //添加审批意见，可在Act_Hi_Comment里的message查询到
        //三个参数分别为待办任务ID,流程实例ID,审批意见
        if(StringUtils.isNotEmpty(dealTaskBean.getComment())) {
            taskService.createComment(dealTaskBean.getTaskId(), dealTaskBean.getProcInstId(), dealTaskBean.getComment());
        }
        if(null != dealTaskBean.getVariables()) {
            taskService.setVariablesLocal(dealTaskBean.getTaskId(), dealTaskBean.getVariables());
        }
        //taskService.setVariableLocal(dealTaskBean.getTaskId(),"变量二","200");
        taskService.complete(dealTaskBean.getTaskId());
        return ResultFactory.buildSuccessResult(null);
    }

    //4、我发起的流程
    @ApiOperation(value = "我发起的流程", httpMethod = "GET")
    @GetMapping("/checkByInitiator")
    public Map<String,Object> checkByInitiator(@RequestParam("userId") String userId) {
        List<HistoricProcessInstance> hiList2 = historyService.createHistoricProcessInstanceQuery().startedBy(userId).list();
        long count = historyService.createHistoricProcessInstanceQuery().startedBy(userId).count();
        String hiList = JSONArray.toJSONString(hiList2,SerializerFeature.IgnoreErrorGetter);
        Map<String,Object> result = new HashMap<>();
        result.put("taskList", hiList);
        result.put("count",count);
        return result;
    }
    @ApiOperation(value = "我发起的流程new", httpMethod = "POST")
    @PostMapping("/checkByInitiatorNew")
    public Result checkByInitiatorNew(@RequestBody SimpleQueryBean sqBean) {
        HistoricProcessInstanceQuery hpiQuery = historyService.createHistoricProcessInstanceQuery();
        if(StringUtils.isNoneBlank(sqBean.getName())){
            hpiQuery.processInstanceBusinessKeyLike("%"+sqBean.getName()+"%");
        }
        if(null != sqBean.getStartTime()){
            hpiQuery.startedAfter(sqBean.getStartTime());
        }
        if(null != sqBean.getEndTime()){
            hpiQuery.startedBefore(sqBean.getEndTime());
        }
        List<HistoricProcessInstance> hiList = hpiQuery
                .startedBy(sqBean.getAssignee())
                .orderByProcessInstanceStartTime()
                .desc()
                .listPage((sqBean.getCurrentPage()-1)*sqBean.getPageSize(),sqBean.getPageSize());
        long count = hpiQuery
                .startedBy(sqBean.getAssignee())
                .count();
        Map<String,Object> result = new HashMap<>();
        result.put("rows", CamundaObjectUtil.objectListToJSONArray(hiList));
        result.put("total",count);
        return ResultFactory.buildSuccessResult(result);
    }
    // 我撤销的流程 TODO
    @ApiIgnore
    @ApiOperation(value = "我撤销/终止的流程", httpMethod = "POST")
    @PostMapping("/checkByInitiatorTerminated")
    public Result checkByInitiatorTerminated(@RequestBody SimpleQueryBean sqBean) {
        HistoricProcessInstanceQuery hpiQuery = historyService.createHistoricProcessInstanceQuery();
        if(StringUtils.isNoneBlank(sqBean.getName())){
            hpiQuery.processInstanceBusinessKeyLike("%"+sqBean.getName()+"%");
        }
        if(null != sqBean.getStartTime()){
            hpiQuery.startedAfter(sqBean.getStartTime());
        }
        if(null != sqBean.getEndTime()){
            hpiQuery.startedBefore(sqBean.getEndTime());
        }
        List<HistoricProcessInstance> hiList = hpiQuery
                .startedBy(sqBean.getAssignee())
                .internallyTerminated()
                .orderByProcessInstanceStartTime()
                .desc()
                .listPage((sqBean.getCurrentPage()-1)*sqBean.getPageSize(),sqBean.getPageSize());
        long count = hpiQuery
                .startedBy(sqBean.getAssignee())
                .count();
        Map<String,Object> result = new HashMap<>();
        result.put("rows", CamundaObjectUtil.objectListToJSONArray(hiList));
        result.put("total",count);
        return ResultFactory.buildSuccessResult(result);
    }


    //5、我的待办流程
    @ApiOperation(value = "我的待办流程",httpMethod = "GET")
    @GetMapping("/getTodoList")
    public String getTodoList(@RequestParam("userId") String userId) {
        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee(userId)
                .orderByTaskCreateTime()
                .desc()
                .list();

        return JSONArray.toJSONString(taskList,SerializerFeature.IgnoreErrorGetter);
    }
    @ApiOperation(value = "我的待办流程new",httpMethod = "POST")
    @PostMapping("/getTodoListNew")
    public Result getTodoListNew(@RequestBody SimpleQueryBean sqBean) throws IllegalAccessException {
        Map<String,Object> params = ObjectUtil.objectToMap(sqBean);
        PageConvert.currentPageConvertStartIndex(params);
        PageBean pageBean = myTaskService.getTodoTaskList(params);
        return ResultFactory.buildSuccessResult(pageBean);
    }

    /**
     * 获取首页我的待办、已办、发起流程数量
     * @param userId 用户id
     * @return 统计数量Map
     */
    @ApiOperation(value = "流程统计",httpMethod = "GET")
    @GetMapping("/getProcessStatistics")
    public Result getProcessStatistics(@RequestParam("userId") String userId) {
        Map<String,Object> result = new HashMap<>();
        long myTodo = taskService.createTaskQuery()
                .taskAssignee(userId)
                .count();
        long myHistory = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(userId)
                .finished()
                .count();
        long myInitiator = historyService.createHistoricProcessInstanceQuery()
                .startedBy(userId)
                .count();
        result.put("myTodo",myTodo);
        result.put("myHistory", myHistory);
        result.put("myInitiator", myInitiator);
        return ResultFactory.buildSuccessResult(result);
    }

    //6、我的已办流程
    @ApiOperation(value = "我的已办流程",httpMethod = "GET")
    @GetMapping("/getHistoryList")
    public Map<String,Object> getHistoryList(@RequestParam("userId") String userId,@RequestParam("currentPage") int currentPage,
                                 @RequestParam("pageSize") int pageSize) {
        List<HistoricTaskInstance> tasks = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(userId)
                .finished()
                .orderByHistoricTaskInstanceEndTime()
                .desc()
                .listPage((currentPage-1)*pageSize,pageSize);
        long count = historyService.createHistoricTaskInstanceQuery()
                .taskAssignee(userId)
                .finished()
                .count();

        String taskList = JSONArray.toJSONString(tasks,SerializerFeature.IgnoreErrorGetter);
        Map<String,Object> result = new HashMap<>();
        result.put("taskList", taskList);
        result.put("total",count);
        return result;
    }
    @ApiOperation(value = "我的已办流程new",httpMethod = "POST")
    @PostMapping("/getHistoryListNew")
    public Result getHistoryListNew(@RequestBody SimpleQueryBean sqBean) throws IllegalAccessException {
        Map<String,Object> params = ObjectUtil.objectToMap(sqBean);
        PageConvert.currentPageConvertStartIndex(params);
        PageBean pageBean = myTaskService.getHistoryTaskList(params);
        return ResultFactory.buildSuccessResult(pageBean);
    }


    //7、流程模板
    @ApiOperation(value = "获取流程模板",httpMethod = "GET")
    @GetMapping("/getProcessModel")
    public byte[] getProcessModel(@ApiParam("procDefId") String procDefId) throws IOException {
        InputStream is = repositoryService.getProcessModel(procDefId);
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int rc;
        while ((rc = is.read(buff, 0, 1024)) > 0) {
            swapStream.write(buff, 0, rc);
        }

        return swapStream.toByteArray();
    }

    @ApiOperation(value = "获取流程定义信息",httpMethod = "GET")
    @GetMapping("/getProcessDefinition")
    public Result getProcessDefinition(@RequestParam("procDefId") String procDefId) {
        ProcessDefinition pd2 = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(procDefId)
                .singleResult();
        return ResultFactory.buildSuccessResult(JSONObject.toJSONString(pd2,SerializerFeature.IgnoreErrorGetter));
    }


    public static File asFile(InputStream inputStream,String pathName) throws IOException {
        File file = new File(pathName);
        OutputStream os = new FileOutputStream(file);
        int bytesRead = 0;
        byte[] buffer = new byte[1024];
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            os.write(buffer, 0, bytesRead);
        }
        inputStream.close();
        return file;
    }

    //service task


    //7、流程任务详情
    @ApiOperation(value = "获取任务详情", httpMethod = "GET")
    @GetMapping("getTaskByTaskId")
    public String getTaskByTaskId(@RequestParam("taskId") String taskId){
        Task task = taskService.createTaskQuery().taskId(taskId).singleResult();

        return JSONObject.toJSONString(task,SerializerFeature.IgnoreErrorGetter);
    }
    @ApiOperation(value = "根据processDefinitionKey获取任务列表", httpMethod = "GET")
    @GetMapping("/getTaskListByProcessDefinitionKey")
    public Result getTaskListByProcessDefinitionKey(@RequestParam("procDefKey") String procDefKey) {
        List<Task> taskList = taskService.createTaskQuery().processDefinitionKey(procDefKey).list();
        taskService.createTaskQuery().processDefinitionKey(procDefKey).count();
        return ResultFactory.buildSuccessResult(JSONArray.toJSONString(taskList, SerializerFeature.IgnoreErrorGetter));
    }
    @ApiOperation(value = "根据processDefinitionKey获取任务数量", httpMethod = "GET")
    @GetMapping("/getTaskCountByProcessDefinitionKey")
    public long getTaskCountByProcessDefinitionKey(@RequestParam("procDefKey") String procDefKey) {

        return taskService.createTaskQuery().processDefinitionKey(procDefKey).count();
    }

    @ApiOperation(value = "根据processInstanceId获取当前用户任务节点(弃用)", httpMethod = "GET")
    @GetMapping("/getHandleUserActivity")
    public Result getHandleUserActivity(@RequestParam("procInstId") String procInstId) {
        List<HistoricTaskInstance> hiTaskList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(procInstId)
                .orderByHistoricActivityInstanceStartTime()
                .desc()
                .list();
        return ResultFactory.buildSuccessResult(CamundaObjectUtil.objectListToJSONArray(hiTaskList));
    }
    @ApiOperation(value = "根据processInstanceId获取当前待办节点", httpMethod = "GET")
    @GetMapping("/getUnfinishedUserActivity")
    public String getUnfinishedUserActivity(@RequestParam("procInstId") String procInstId) {
        //只返回当前待办的一个节点
        List<HistoricTaskInstance> hiTaskList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(procInstId)
                .unfinished()
                .orderByHistoricActivityInstanceStartTime()
                .desc()
                .list();
        return JSONObject.toJSONString(hiTaskList, SerializerFeature.IgnoreErrorGetter);
    }
    @ApiOperation(value = "根据processInstanceId获取所有经办节点", httpMethod = "GET")
    @GetMapping("/getHandleActivity")
    public Result getHandleActivity(@RequestParam("procInstId") String procInstId) {
        List<HistoricActivityInstance> finishedList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(procInstId)
                .finished()
                .orderByHistoricActivityInstanceStartTime()
                .asc()
                .list();
        return ResultFactory.buildSuccessResult(CamundaObjectUtil.objectListToJSONArray(finishedList));
    }

    @ApiOperation(value = "根据procInstId获取渲染节点", httpMethod = "GET")
    @GetMapping("/getRenderingActivity")
    public Result getRenderingActivity(@RequestParam("procInstId") String procInstId) {
        List<HistoricActivityInstance> hiTaskList = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(procInstId)
                .orderByHistoricActivityInstanceStartTime()
                .asc()
                .list();
        List<HistoricActivityInstance> resultList = new ArrayList<>();
        //获取最后一个节点，即待办节点
        HistoricActivityInstance todoHai = hiTaskList.get(hiTaskList.size()-1);
        //返回到待办节点之前的顺序不重复的节点list
        LinkedHashMap<String,Object> linkedHashMap = new LinkedHashMap();
        for(HistoricActivityInstance hai : hiTaskList){
            linkedHashMap.put(hai.getActivityId(),hai);
        }
        ListIterator<Map.Entry<String,Object>> li = new ArrayList<>(linkedHashMap.entrySet()).listIterator();
        while (li.hasNext()) {
            Map.Entry<String, Object> entry = li.next();
            resultList.add((HistoricActivityInstance) entry.getValue());
            if(todoHai.getActivityId().equals(entry.getKey())){
                break;
            }
        }
        return ResultFactory.buildSuccessResult(CamundaObjectUtil.objectListToJSONArray(resultList));
    }



    //8、节点对应的所有变量（区分不同变量类型情况）
    public Result getNodeVariables(@RequestParam("procInstId") String procInstId){
        //

        return ResultFactory.buildSuccessResult(null);
    }


    //9、流程转发


    //10、获取用户列表
    @ApiOperation(value = "获取用户列表",httpMethod = "POST")
    @PostMapping("/getUserList")
    public Result getUserList(@RequestBody SimpleQueryBean sqBean){
        UserQuery uq = identityService.createUserQuery();
        if(StringUtils.isNotEmpty(sqBean.getName())){
            uq.userFirstNameLike(sqBean.getName());
        }
        List<User> userList = uq.listPage((sqBean.getCurrentPage()-1)*sqBean.getPageSize(),sqBean.getPageSize());
        long userTotal = uq.count();
        Map<String,Object> result = new HashMap<>();
        result.put("userList", CamundaObjectUtil.objectListToJSONArray(userList,User.class));
        result.put("total",userTotal);
        return ResultFactory.buildSuccessResult(result);
    }
    /**
     * 由camunda自动为数据库加密加盐
     */
    @ApiOperation(value = "添加用户(返回用户ID)",httpMethod = "POST")
    @PostMapping
    public Result addUser(@RequestBody AddUser addUser) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(addUser.getId());
        userEntity.setFirstName(addUser.getFirstName());
        userEntity.setLastName(addUser.getLastName());
        userEntity.setEmail(addUser.getEmail());
        userEntity.setPassword(addUser.getPwd());
        identityService.saveUser(userEntity);
        return ResultFactory.buildSuccessResult(addUser.getId());
    }
    /**
     * 编辑组
     */
    @ApiOperation(value = "编辑用户", httpMethod = "GET")
    @GetMapping("/updateUser")
    public Result updateUser(String userId, String firstName) {
        if(StringUtils.isEmpty(userId)){
            return ResultFactory.buildFailResult("用户id不能为空");
        }
        ActIdUser user = new ActIdUser();
        user.setId(userId);
        user.setFirst(StringUtils.isNotEmpty(firstName)?firstName:null);
        //user.setPwd(StringUtils.isNotEmpty(pwd)?pwd:null);
        myIdentityService.updateUser(user);
        return ResultFactory.buildSuccessResult(null);
    }


    public void  setUserPicture(String fileUrl){
        String userId="zhangsan";
        byte[] bytes = IoUtil.fileAsByteArray(new File("src/main/resources/2.jpg"));
        Picture picture=new Picture(bytes,"jpg");
        identityService.setUserPicture(userId,picture);
    }
    /**
     * 租户存在的目的就是区分不同的系统，A系统和B系统的用户是不一样的，所以要区分
     * A租户下有哪些用户,A租户下有哪些组
     * insert into ACT_ID_TENANT (ID_, NAME_, REV_) values ( ?, ?, 1 )
     * Parameters: A(String), A系统(String)
     */
    public  void saveTenant(){
        TenantEntity tenantEntity=new TenantEntity();
        tenantEntity.setId("A");
        tenantEntity.setName("A系统");
        identityService.saveTenant(tenantEntity);
    }
    /**
     * 新增组
     */
    @ApiOperation(value = "添加组(返回组id)", httpMethod = "POST")
    @PostMapping("/addGroup")
    public Result addGroup(@RequestBody AddGroup group) {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setId(group.getId());
        groupEntity.setName(group.getName());
        groupEntity.setRevision(group.getRev());
        groupEntity.setType(group.getType());
        identityService.saveGroup(groupEntity);
        return ResultFactory.buildSuccessResult(group.getId());
    }
    /**
     * 编辑组
     */
    @ApiOperation(value = "编辑组", httpMethod = "GET")
    @GetMapping("/updateGroup")
    public Result updateGroup(String groupId, String groupName,String groupType) {
        if(StringUtils.isEmpty(groupId)){
            return ResultFactory.buildFailResult("组id不能为空");
        }
        ActIdGroup group = new ActIdGroup();
        group.setId(groupId);
        group.setName(StringUtils.isNotEmpty(groupName)?groupName:null);
        group.setType(StringUtils.isNotEmpty(groupType)?groupType:null);
        myIdentityService.updateGroup(group);
        return ResultFactory.buildSuccessResult(null);
    }

    //11、获取组列表：收样组、质检组、抽提组、实验组、报告组
    @ApiOperation(value = "获取组列表",httpMethod = "POST")
    @PostMapping("/getGroupList")
    public Result getGroupList(@RequestBody SimpleQueryBean sqBean){
        GroupQuery gq = identityService.createGroupQuery();
        if(StringUtils.isNotEmpty(sqBean.getName())){
            gq.groupNameLike(sqBean.getName());
        }
        List<Group> groupList = gq.listPage((sqBean.getCurrentPage()-1)*sqBean.getPageSize(),sqBean.getPageSize());
        long groupTotal = gq.count();
        Map<String,Object> result = new HashMap<>();
        result.put("groupList", CamundaObjectUtil.objectListToJSONArray(groupList,Group.class));
        result.put("total",groupTotal);
        return ResultFactory.buildSuccessResult(result);
    }

    /**
     * 将用户添加到组
     */
    @ApiOperation(value = "将用户添加到组",httpMethod = "POST")
    @PostMapping("/createMembership")
    public Result createMembership(@RequestBody Membership membership) {
        if(membership == null || membership.getGroupIdList() == null || membership.getUserId() == null){
            return ResultFactory.buildFailResult("groupId或userId不能为空");
        }
        for(String groupId : membership.getGroupIdList()) {
            identityService.createMembership(membership.getUserId(), groupId);
        }
        return ResultFactory.buildSuccessResult(null);
    }
    /**
     * 将用户从组中移除
     */
    @ApiOperation(value = "将用户从组中移除",httpMethod = "GET")
    @GetMapping("/delMembership")
    public Result delMembership(@RequestParam("userId") String userId,@RequestParam("groupId")  String groupId) {
        identityService.deleteMembership(userId, groupId);
        return ResultFactory.buildSuccessResult(null);
    }
    /**
     * 删除用户
     */
    @ApiOperation(value = "删除用户",httpMethod = "GET")
    @GetMapping("/delUser")
    public Result delUser(@RequestParam("userId") String userId) {
        identityService.deleteUser(userId);
        return ResultFactory.buildSuccessResult(null);
    }
    /**
     * 删除组
     */
    @ApiOperation(value = "删除组(同时删除拥有该组的用户的组)",httpMethod = "GET")
    @GetMapping("/delGroup")
    public Result delGroup(@RequestParam("groupId")  String groupId) {
        identityService.deleteGroup(groupId);
        return ResultFactory.buildSuccessResult(null);
    }


    //12、获取组用户列表
    @ApiOperation(value = "获取组用户列表",httpMethod = "GET")
    @GetMapping("/getGroupUserList")
    public Result getGroupUserList(@RequestParam("groupId")  String groupId) {
        List<User> userList = identityService.createUserQuery().memberOfGroup(groupId).list();

        return ResultFactory.buildSuccessResult(CamundaObjectUtil.objectListToJSONArray(userList,User.class));
    }
    @ApiOperation(value = "获取用户所在组列表",httpMethod = "GET")
    @GetMapping("/getUserGroupList")
    public Result getUserGroupList(@RequestParam("userId")  String userId) {
        List<Group> groupList = identityService.createGroupQuery()
                .groupMember(userId)
                .list();
        return ResultFactory.buildSuccessResult(CamundaObjectUtil.objectListToJSONArray(groupList,Group.class));
    }

    @ApiOperation(value = "获取组任务",httpMethod = "GET")
    @GetMapping("/getGroupTaskList")
    public Result getGroupTaskList(@RequestParam("groupId")  String groupId) {
        List<Task> taskList = taskService.createTaskQuery().taskCandidateGroup(groupId).list();
        return ResultFactory.buildSuccessResult(CamundaObjectUtil.objectListToJSONArray(taskList,Task.class));
    }
    @ApiOperation(value = "认领任务",httpMethod = "GET")
    @GetMapping("/claimTask")
    public Result claimTask(@RequestParam("userId")  String userId,@RequestParam("taskId")  String taskId) {
        taskService.claim(taskId,userId);
        return ResultFactory.buildSuccessResult(null);
    }

    @PostMapping("/my")
    public void  my(@RequestBody List<Long> ids){
        for(Long id : ids){
            System.out.println("id === > "+id );
        }

    }




    public static void main(String[] args) {
        Deployment de=new Deployment() {
            @Override
            public String getId() {
                return "a";
            }

            @Override
            public String getName() {
                return null;
            }

            @Override
            public Date getDeploymentTime() {
                return null;
            }

            @Override
            public String getSource() {
                return null;
            }

            @Override
            public String getTenantId() {
                return null;
            }
        };

        List<Deployment> list = new ArrayList<>();
        list.add(de);
        System.out.println(JSONObject.toJSONString(list));

        System.out.println((new Date()).getTime());


    }




}
