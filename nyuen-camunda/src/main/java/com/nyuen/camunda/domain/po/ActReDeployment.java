package com.nyuen.camunda.domain.po;

import java.util.Date;

public class ActReDeployment {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column act_re_deployment.ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    private String id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column act_re_deployment.NAME_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    private String name;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column act_re_deployment.DEPLOY_TIME_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    private Date deployTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column act_re_deployment.SOURCE_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    private String source;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column act_re_deployment.TENANT_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    private String tenantId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column act_re_deployment.ID_
     *
     * @return the value of act_re_deployment.ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public String getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column act_re_deployment.ID_
     *
     * @param id the value for act_re_deployment.ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column act_re_deployment.NAME_
     *
     * @return the value of act_re_deployment.NAME_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public String getName() {
        return name;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column act_re_deployment.NAME_
     *
     * @param name the value for act_re_deployment.NAME_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column act_re_deployment.DEPLOY_TIME_
     *
     * @return the value of act_re_deployment.DEPLOY_TIME_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public Date getDeployTime() {
        return deployTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column act_re_deployment.DEPLOY_TIME_
     *
     * @param deployTime the value for act_re_deployment.DEPLOY_TIME_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public void setDeployTime(Date deployTime) {
        this.deployTime = deployTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column act_re_deployment.SOURCE_
     *
     * @return the value of act_re_deployment.SOURCE_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public String getSource() {
        return source;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column act_re_deployment.SOURCE_
     *
     * @param source the value for act_re_deployment.SOURCE_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public void setSource(String source) {
        this.source = source == null ? null : source.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column act_re_deployment.TENANT_ID_
     *
     * @return the value of act_re_deployment.TENANT_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public String getTenantId() {
        return tenantId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column act_re_deployment.TENANT_ID_
     *
     * @param tenantId the value for act_re_deployment.TENANT_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public void setTenantId(String tenantId) {
        this.tenantId = tenantId == null ? null : tenantId.trim();
    }
}