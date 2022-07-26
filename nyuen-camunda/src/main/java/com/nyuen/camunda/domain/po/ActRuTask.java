package com.nyuen.camunda.domain.po;

import java.util.Date;

public class ActRuTask {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column act_ru_task.ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    private String id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column act_ru_task.REV_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    private Integer rev;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column act_ru_task.EXECUTION_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    private String executionId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column act_ru_task.PROC_INST_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    private String procInstId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column act_ru_task.PROC_DEF_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    private String procDefId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column act_ru_task.CASE_EXECUTION_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    private String caseExecutionId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column act_ru_task.CASE_INST_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    private String caseInstId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column act_ru_task.CASE_DEF_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    private String caseDefId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column act_ru_task.NAME_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column act_ru_task.PARENT_TASK_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    private String parentTaskId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column act_ru_task.DESCRIPTION_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    private String description;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column act_ru_task.TASK_DEF_KEY_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    private String taskDefKey;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column act_ru_task.OWNER_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    private String owner;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column act_ru_task.ASSIGNEE_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    private String assignee;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column act_ru_task.DELEGATION_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    private String delegation;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column act_ru_task.PRIORITY_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    private Integer priority;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column act_ru_task.CREATE_TIME_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column act_ru_task.DUE_DATE_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    private Date dueDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column act_ru_task.FOLLOW_UP_DATE_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    private Date followUpDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column act_ru_task.SUSPENSION_STATE_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    private Integer suspensionState;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column act_ru_task.TENANT_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    private String tenantId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column act_ru_task.ID_
     *
     * @return the value of act_ru_task.ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column act_ru_task.ID_
     *
     * @param id the value for act_ru_task.ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column act_ru_task.REV_
     *
     * @return the value of act_ru_task.REV_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public Integer getRev() {
        return rev;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column act_ru_task.REV_
     *
     * @param rev the value for act_ru_task.REV_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public void setRev(Integer rev) {
        this.rev = rev;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column act_ru_task.EXECUTION_ID_
     *
     * @return the value of act_ru_task.EXECUTION_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public String getExecutionId() {
        return executionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column act_ru_task.EXECUTION_ID_
     *
     * @param executionId the value for act_ru_task.EXECUTION_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public void setExecutionId(String executionId) {
        this.executionId = executionId == null ? null : executionId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column act_ru_task.PROC_INST_ID_
     *
     * @return the value of act_ru_task.PROC_INST_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public String getProcInstId() {
        return procInstId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column act_ru_task.PROC_INST_ID_
     *
     * @param procInstId the value for act_ru_task.PROC_INST_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public void setProcInstId(String procInstId) {
        this.procInstId = procInstId == null ? null : procInstId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column act_ru_task.PROC_DEF_ID_
     *
     * @return the value of act_ru_task.PROC_DEF_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public String getProcDefId() {
        return procDefId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column act_ru_task.PROC_DEF_ID_
     *
     * @param procDefId the value for act_ru_task.PROC_DEF_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public void setProcDefId(String procDefId) {
        this.procDefId = procDefId == null ? null : procDefId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column act_ru_task.CASE_EXECUTION_ID_
     *
     * @return the value of act_ru_task.CASE_EXECUTION_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public String getCaseExecutionId() {
        return caseExecutionId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column act_ru_task.CASE_EXECUTION_ID_
     *
     * @param caseExecutionId the value for act_ru_task.CASE_EXECUTION_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public void setCaseExecutionId(String caseExecutionId) {
        this.caseExecutionId = caseExecutionId == null ? null : caseExecutionId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column act_ru_task.CASE_INST_ID_
     *
     * @return the value of act_ru_task.CASE_INST_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public String getCaseInstId() {
        return caseInstId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column act_ru_task.CASE_INST_ID_
     *
     * @param caseInstId the value for act_ru_task.CASE_INST_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public void setCaseInstId(String caseInstId) {
        this.caseInstId = caseInstId == null ? null : caseInstId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column act_ru_task.CASE_DEF_ID_
     *
     * @return the value of act_ru_task.CASE_DEF_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public String getCaseDefId() {
        return caseDefId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column act_ru_task.CASE_DEF_ID_
     *
     * @param caseDefId the value for act_ru_task.CASE_DEF_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public void setCaseDefId(String caseDefId) {
        this.caseDefId = caseDefId == null ? null : caseDefId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column act_ru_task.NAME_
     *
     * @return the value of act_ru_task.NAME_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column act_ru_task.NAME_
     *
     * @param name the value for act_ru_task.NAME_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column act_ru_task.PARENT_TASK_ID_
     *
     * @return the value of act_ru_task.PARENT_TASK_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public String getParentTaskId() {
        return parentTaskId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column act_ru_task.PARENT_TASK_ID_
     *
     * @param parentTaskId the value for act_ru_task.PARENT_TASK_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public void setParentTaskId(String parentTaskId) {
        this.parentTaskId = parentTaskId == null ? null : parentTaskId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column act_ru_task.DESCRIPTION_
     *
     * @return the value of act_ru_task.DESCRIPTION_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public String getDescription() {
        return description;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column act_ru_task.DESCRIPTION_
     *
     * @param description the value for act_ru_task.DESCRIPTION_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column act_ru_task.TASK_DEF_KEY_
     *
     * @return the value of act_ru_task.TASK_DEF_KEY_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public String getTaskDefKey() {
        return taskDefKey;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column act_ru_task.TASK_DEF_KEY_
     *
     * @param taskDefKey the value for act_ru_task.TASK_DEF_KEY_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public void setTaskDefKey(String taskDefKey) {
        this.taskDefKey = taskDefKey == null ? null : taskDefKey.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column act_ru_task.OWNER_
     *
     * @return the value of act_ru_task.OWNER_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public String getOwner() {
        return owner;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column act_ru_task.OWNER_
     *
     * @param owner the value for act_ru_task.OWNER_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public void setOwner(String owner) {
        this.owner = owner == null ? null : owner.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column act_ru_task.ASSIGNEE_
     *
     * @return the value of act_ru_task.ASSIGNEE_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public String getAssignee() {
        return assignee;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column act_ru_task.ASSIGNEE_
     *
     * @param assignee the value for act_ru_task.ASSIGNEE_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public void setAssignee(String assignee) {
        this.assignee = assignee == null ? null : assignee.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column act_ru_task.DELEGATION_
     *
     * @return the value of act_ru_task.DELEGATION_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public String getDelegation() {
        return delegation;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column act_ru_task.DELEGATION_
     *
     * @param delegation the value for act_ru_task.DELEGATION_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public void setDelegation(String delegation) {
        this.delegation = delegation == null ? null : delegation.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column act_ru_task.PRIORITY_
     *
     * @return the value of act_ru_task.PRIORITY_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column act_ru_task.PRIORITY_
     *
     * @param priority the value for act_ru_task.PRIORITY_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column act_ru_task.CREATE_TIME_
     *
     * @return the value of act_ru_task.CREATE_TIME_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column act_ru_task.CREATE_TIME_
     *
     * @param createTime the value for act_ru_task.CREATE_TIME_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column act_ru_task.DUE_DATE_
     *
     * @return the value of act_ru_task.DUE_DATE_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public Date getDueDate() {
        return dueDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column act_ru_task.DUE_DATE_
     *
     * @param dueDate the value for act_ru_task.DUE_DATE_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column act_ru_task.FOLLOW_UP_DATE_
     *
     * @return the value of act_ru_task.FOLLOW_UP_DATE_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public Date getFollowUpDate() {
        return followUpDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column act_ru_task.FOLLOW_UP_DATE_
     *
     * @param followUpDate the value for act_ru_task.FOLLOW_UP_DATE_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public void setFollowUpDate(Date followUpDate) {
        this.followUpDate = followUpDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column act_ru_task.SUSPENSION_STATE_
     *
     * @return the value of act_ru_task.SUSPENSION_STATE_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public Integer getSuspensionState() {
        return suspensionState;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column act_ru_task.SUSPENSION_STATE_
     *
     * @param suspensionState the value for act_ru_task.SUSPENSION_STATE_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public void setSuspensionState(Integer suspensionState) {
        this.suspensionState = suspensionState;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column act_ru_task.TENANT_ID_
     *
     * @return the value of act_ru_task.TENANT_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public String getTenantId() {
        return tenantId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column act_ru_task.TENANT_ID_
     *
     * @param tenantId the value for act_ru_task.TENANT_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId == null ? null : tenantId.trim();
    }
}