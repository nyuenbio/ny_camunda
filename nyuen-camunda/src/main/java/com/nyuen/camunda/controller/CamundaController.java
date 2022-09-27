package com.nyuen.camunda.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.nyuen.camunda.domain.vo.TodoTask;
import com.nyuen.camunda.result.Result;
import com.nyuen.camunda.result.ResultFactory;
import com.nyuen.camunda.service.MyTaskService;
import com.nyuen.camunda.vo.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.camunda.bpm.engine.*;
import org.camunda.bpm.engine.form.TaskFormData;
import org.camunda.bpm.engine.history.*;
import org.camunda.bpm.engine.identity.Group;
import org.camunda.bpm.engine.identity.Picture;
import org.camunda.bpm.engine.identity.User;
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
        // todo 有bug???
        InputStream is = formService.getDeployedTaskForm(taskId);

        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int rc = 0;
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
    public String getProcessVariable(@RequestParam("processInstanceId") String processInstanceId) {
        //不适用于已办节点
        //Object obj = formService.getRenderedTaskForm(taskId);
        //不适用于已办节点
        //TaskFormData tfData = formService.getTaskFormData(taskId);
        List<HistoricVariableInstance> hviList = historyService.createHistoricVariableInstanceQuery()
                .processInstanceId(processInstanceId)
                .list();
        // todo 不适用于待办节点
        List<HistoricVariableInstance> hviList2 = historyService.createHistoricVariableInstanceQuery()
                .processInstanceIdIn(processInstanceId)
                .listPage(0,10);
        return JSONArray.toJSONString(hviList2,SerializerFeature.IgnoreErrorGetter);
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
    public String getDeployFormList() {
        List<Deployment> deployFormList=repositoryService.createDeploymentQuery()
                .deploymentNameLike("%\\_form")
                .orderByDeploymentTime()
                .desc()
                .list();

        return JSONArray.toJSONString(deployFormList, SerializerFeature.IgnoreErrorGetter);
    }
    @ApiOperation(value = "获取已部署的form内容",httpMethod = "GET")
    @GetMapping("/getDeployForm")
    public byte[] getDeployForm(String deploymentId,String name) throws IOException {
        //List<String> deployFormList=repositoryService.getDeploymentResourceNames(deploymentId);
        InputStream is = repositoryService.getResourceAsStream(deploymentId,name+".form");
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int rc = 0;
        while ((rc = is.read(buff, 0, 1024)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        return swapStream.toByteArray();
    }
    @GetMapping("/getDeployForm2")
    public Object getDeployForm2(String deploymentId,String name) throws IOException {
        //List<String> deployFormList=repositoryService.getDeploymentResourceNames(deploymentId);
        InputStream is = null;
        try {
            is = repositoryService.getResourceAsStream(deploymentId,name+".form");
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int rc = 0;
        while ((rc = is.read(buff, 0, 1024)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        return swapStream.toByteArray();
    }


    //2、开启流程
    @ApiOperation(value = "开启流程",httpMethod = "POST")
    @PostMapping("/startProcess")
    public String startProcess(@RequestBody StartProcessBean startProcessBean){
        identityService.setAuthenticatedUserId(startProcessBean.getInitiator());
        runtimeService.startProcessInstanceById(startProcessBean.getProcessDefinitionId(),
                startProcessBean.getBusinessKey(),startProcessBean.getVariables());

        return null;
    }

    //3、处理流程节点
    @ApiOperation(value = "处理流程节点(带全局变量的节点)",httpMethod = "POST")
    @PostMapping("/dealTask")
    public String dealTask(@RequestBody DealTaskBean dealTaskBean){
        //添加审批人
        processEngine.getIdentityService().setAuthenticatedUserId(dealTaskBean.getAssignee());
        //添加审批意见，可在Act_Hi_Comment里的message查询到
        //三个参数分别为待办任务ID,流程实例ID,审批意见
        if(null != dealTaskBean.getComment()) {
            taskService.createComment(dealTaskBean.getTaskId(), dealTaskBean.getProcessInstanceId(), dealTaskBean.getComment());
        }
        //处理节点变量
        if(null != dealTaskBean.getVariables()) {
            taskService.setVariables(dealTaskBean.getTaskId(), dealTaskBean.getVariables());
        }
        //处理节点表单 TODO
        //formService.submitTaskForm(dealTaskBean.getTaskId(), dealTaskBean.getProperties());
        //任务完成,也就是审批通过
        taskService.complete(dealTaskBean.getTaskId());

        return "";
    }
    @ApiOperation(value = "处理流程节点(带局部变量的节点)",httpMethod = "POST")
    @PostMapping("/dealTaskLocal")
    public String dealTaskLocal(@RequestBody DealTaskBean dealTaskBean){
        //添加审批人
        processEngine.getIdentityService().setAuthenticatedUserId(dealTaskBean.getAssignee());
        //添加审批意见，可在Act_Hi_Comment里的message查询到
        //三个参数分别为待办任务ID,流程实例ID,审批意见
        if(null != dealTaskBean.getComment()) {
            taskService.createComment(dealTaskBean.getTaskId(), dealTaskBean.getProcessInstanceId(), dealTaskBean.getComment());
        }
        if(null != dealTaskBean.getVariables()) {
            taskService.setVariablesLocal(dealTaskBean.getTaskId(), dealTaskBean.getVariables());
        }
        //taskService.setVariableLocal(dealTaskBean.getTaskId(),"变量二","200");
        taskService.complete(dealTaskBean.getTaskId());
        return "";
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

    //5、我的待办流程
    @ApiOperation(value = "我的待办流程",httpMethod = "GET")
    @GetMapping("/getTodoList")
    public String getTodoList(@RequestParam("userId") String userId) {
        List<Task> taskList = taskService.createTaskQuery()
                .taskAssignee(userId)
                .orderByTaskCreateTime()
                .desc()
                .list();

        String result = JSONArray.toJSONString(taskList,SerializerFeature.IgnoreErrorGetter);
        return result;
    }
    @ApiOperation(value = "我的待办流程new",httpMethod = "GET")
    @GetMapping("/getTodoList2")
    public String getTodoList2(@RequestParam("userId") String userId) {
        Map<String,Object> params = new HashMap<>();
        params.put("assignee",userId);
        List<TodoTask> todoTaskList= myTaskService.getTodoTaskList(params);
        return JSONArray.toJSONString(todoTaskList);
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

    //7、流程模板
    @ApiOperation(value = "获取流程模板",httpMethod = "GET")
    @GetMapping("/getProcessModel")
    public byte[] getProcessModel(@ApiParam("processDefinitionId") String processDefinitionId) throws IOException {
        InputStream is = repositoryService.getProcessModel(processDefinitionId);
        ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int rc = 0;
        while ((rc = is.read(buff, 0, 1024)) > 0) {
            swapStream.write(buff, 0, rc);
        }
        return swapStream.toByteArray();
    }

    @ApiOperation(value = "获取流程定义信息",httpMethod = "GET")
    @GetMapping("/getProcessDefinition")
    public String getProcessDefinition(@RequestParam("processDefinitionId") String processDefinitionId) {
        ProcessDefinition pd2 = repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processDefinitionId)
                .singleResult();
        return JSONObject.toJSONString(pd2,SerializerFeature.IgnoreErrorGetter);
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
    public String getTaskListByProcessDefinitionKey(@RequestParam("processDefinitionKey") String processDefinitionKey) {
        List<Task> taskList = taskService.createTaskQuery().processDefinitionKey(processDefinitionKey).list();
        taskService.createTaskQuery().processDefinitionKey(processDefinitionKey).count();
        return JSONArray.toJSONString(taskList, SerializerFeature.IgnoreErrorGetter);
    }
    @ApiOperation(value = "根据processDefinitionKey获取任务数量", httpMethod = "GET")
    @GetMapping("/getTaskCountByProcessDefinitionKey")
    public long getTaskCountByProcessDefinitionKey(@RequestParam("processDefinitionKey") String processDefinitionKey) {

        return taskService.createTaskQuery().processDefinitionKey(processDefinitionKey).count();
    }

    @ApiOperation(value = "根据processInstanceId获取当前用户任务节点", httpMethod = "GET")
    @GetMapping("/getHandleUserActivity")
    public String getHandleUserActivity(@RequestParam("processInstanceId") String processInstanceId) {
        List<HistoricTaskInstance> hiTaskList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .orderByHistoricActivityInstanceStartTime()
                .desc()
                .list();
        return JSONObject.toJSONString(hiTaskList, SerializerFeature.IgnoreErrorGetter);
    }
    @ApiOperation(value = "根据processInstanceId获取当前待办节点", httpMethod = "GET")
    @GetMapping("/getUnfinishedUserActivity")
    public String getUnfinishedUserActivity(@RequestParam("processInstanceId") String processInstanceId) {
        List<HistoricTaskInstance> hiTaskList = historyService.createHistoricTaskInstanceQuery()
                .processInstanceId(processInstanceId)
                .unfinished()
                .orderByHistoricActivityInstanceStartTime()
                .desc()
                .list();
        return JSONObject.toJSONString(hiTaskList, SerializerFeature.IgnoreErrorGetter);
    }
    @ApiOperation(value = "根据processInstanceId获取所有经办节点", httpMethod = "GET")
    @GetMapping("/getHandleActivity")
    public String getHandleActivity(@RequestParam("processInstanceId") String processInstanceId) {
        List<HistoricActivityInstance> finished = historyService.createHistoricActivityInstanceQuery()
                .processInstanceId(processInstanceId)
                .finished()
                .orderByHistoricActivityInstanceStartTime()
                .asc()
                .list();

        return JSONObject.toJSONString(finished,SerializerFeature.IgnoreErrorGetter);
    }


    //8、流程节点详情


    //9、流程转发

    //10、获取用户列表
    @ApiOperation(value = "获取用户列表",httpMethod = "GET")
    @GetMapping("/getUserList")
    public String getUserList(){
        List<User> userList = identityService.createUserQuery().list();
        return JSONArray.toJSONString(userList,SerializerFeature.IgnoreErrorGetter);
    }
    /**
     * 由camunda自动为数据库加密加盐
     */
    @ApiOperation(value = "添加用户(返回用户ID)",httpMethod = "POST")
    @PostMapping
    public String addUser(@RequestBody AddUser addUser) {
        UserEntity userEntity = new UserEntity();
        userEntity.setId(addUser.getId());
        userEntity.setFirstName(addUser.getFirstName());
        userEntity.setLastName(addUser.getLastName());
        userEntity.setEmail(addUser.getEmail());
        userEntity.setPassword(addUser.getPwd());
        identityService.saveUser(userEntity);
        return addUser.getId();
    }
    public void  setUserPicture(){
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
    public String addGroup(@RequestBody AddGroup group) {
        GroupEntity groupEntity = new GroupEntity();
        groupEntity.setId(group.getId());
        groupEntity.setName(group.getName());
        groupEntity.setRevision(group.getRev());
        groupEntity.setType(group.getType());
        identityService.saveGroup(groupEntity);
        return group.getId();
    }
    //11、获取组列表：收样组、质检组、抽提组、实验组、报告组
    @ApiOperation(value = "获取组列表",httpMethod = "GET")
    @GetMapping("/getGroupList")
    public String getGroupList(){
        List<Group> groupList = identityService.createGroupQuery().list();
        return JSONArray.toJSONString(groupList,SerializerFeature.IgnoreErrorGetter);
    }

    /**
     * 将用户添加到组
     */
    @ApiOperation(value = "将用户添加到组",httpMethod = "GET")
    @GetMapping("/createMembership")
    public void createMembership(@RequestParam("userId") String userId,@RequestParam("groupId")  String groupId) {
        identityService.createMembership(userId, groupId);
    }
    //12、获取组用户列表
    @ApiOperation(value = "获取组用户列表",httpMethod = "GET")
    @GetMapping("/getGroupUserList")
    public String getGroupUserList(@RequestParam("groupId")  String groupId) {
        List<User> userList = identityService.createUserQuery().memberOfGroup(groupId).list();
        return JSONArray.toJSONString(userList,SerializerFeature.IgnoreErrorGetter);
    }
    @ApiOperation(value = "获取组任务",httpMethod = "GET")
    @GetMapping("/getGroupTaskList")
    public String getGroupTaskList(@RequestParam("groupId")  String groupId) {
        List<Task> taskList = taskService.createTaskQuery().taskCandidateGroup(groupId).list();
        return JSONArray.toJSONString(taskList,SerializerFeature.IgnoreErrorGetter);
    }
    @ApiOperation(value = "认领任务",httpMethod = "GET")
    @GetMapping("/claimTask")
    public void claimTask(@RequestParam("userId")  String userId,@RequestParam("taskId")  String taskId) {
        taskService.claim(taskId,userId);
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
