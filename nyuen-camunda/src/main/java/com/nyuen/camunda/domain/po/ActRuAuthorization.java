package com.nyuen.camunda.domain.po;

import java.util.Date;

public class ActRuAuthorization {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column act_ru_authorization.ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    private String id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column act_ru_authorization.REV_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    private Integer rev;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column act_ru_authorization.TYPE_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    private Integer type;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column act_ru_authorization.GROUP_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    private String groupId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column act_ru_authorization.USER_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    private String userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column act_ru_authorization.RESOURCE_TYPE_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    private Integer resourceType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column act_ru_authorization.RESOURCE_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    private String resourceId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column act_ru_authorization.PERMS_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    private Integer perms;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column act_ru_authorization.REMOVAL_TIME_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    private Date removalTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column act_ru_authorization.ROOT_PROC_INST_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    private String rootProcInstId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column act_ru_authorization.ID_
     *
     * @return the value of act_ru_authorization.ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column act_ru_authorization.ID_
     *
     * @param id the value for act_ru_authorization.ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column act_ru_authorization.REV_
     *
     * @return the value of act_ru_authorization.REV_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public Integer getRev() {
        return rev;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column act_ru_authorization.REV_
     *
     * @param rev the value for act_ru_authorization.REV_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public void setRev(Integer rev) {
        this.rev = rev;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column act_ru_authorization.TYPE_
     *
     * @return the value of act_ru_authorization.TYPE_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public Integer getType() {
        return type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column act_ru_authorization.TYPE_
     *
     * @param type the value for act_ru_authorization.TYPE_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column act_ru_authorization.GROUP_ID_
     *
     * @return the value of act_ru_authorization.GROUP_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column act_ru_authorization.GROUP_ID_
     *
     * @param groupId the value for act_ru_authorization.GROUP_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId == null ? null : groupId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column act_ru_authorization.USER_ID_
     *
     * @return the value of act_ru_authorization.USER_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public String getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column act_ru_authorization.USER_ID_
     *
     * @param userId the value for act_ru_authorization.USER_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column act_ru_authorization.RESOURCE_TYPE_
     *
     * @return the value of act_ru_authorization.RESOURCE_TYPE_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public Integer getResourceType() {
        return resourceType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column act_ru_authorization.RESOURCE_TYPE_
     *
     * @param resourceType the value for act_ru_authorization.RESOURCE_TYPE_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public void setResourceType(Integer resourceType) {
        this.resourceType = resourceType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column act_ru_authorization.RESOURCE_ID_
     *
     * @return the value of act_ru_authorization.RESOURCE_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public String getResourceId() {
        return resourceId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column act_ru_authorization.RESOURCE_ID_
     *
     * @param resourceId the value for act_ru_authorization.RESOURCE_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public void setResourceId(String resourceId) {
        this.resourceId = resourceId == null ? null : resourceId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column act_ru_authorization.PERMS_
     *
     * @return the value of act_ru_authorization.PERMS_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public Integer getPerms() {
        return perms;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column act_ru_authorization.PERMS_
     *
     * @param perms the value for act_ru_authorization.PERMS_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public void setPerms(Integer perms) {
        this.perms = perms;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column act_ru_authorization.REMOVAL_TIME_
     *
     * @return the value of act_ru_authorization.REMOVAL_TIME_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public Date getRemovalTime() {
        return removalTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column act_ru_authorization.REMOVAL_TIME_
     *
     * @param removalTime the value for act_ru_authorization.REMOVAL_TIME_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public void setRemovalTime(Date removalTime) {
        this.removalTime = removalTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column act_ru_authorization.ROOT_PROC_INST_ID_
     *
     * @return the value of act_ru_authorization.ROOT_PROC_INST_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public String getRootProcInstId() {
        return rootProcInstId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column act_ru_authorization.ROOT_PROC_INST_ID_
     *
     * @param rootProcInstId the value for act_ru_authorization.ROOT_PROC_INST_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public void setRootProcInstId(String rootProcInstId) {
        this.rootProcInstId = rootProcInstId == null ? null : rootProcInstId.trim();
    }
}