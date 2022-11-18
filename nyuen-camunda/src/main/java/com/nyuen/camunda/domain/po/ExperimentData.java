package com.nyuen.camunda.domain.po;

import java.util.Date;

public class ExperimentData {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column experiment_data.id
     *
     * @mbg.generated Fri Nov 18 13:53:53 CST 2022
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column experiment_data.file_name
     *
     * @mbg.generated Fri Nov 18 13:53:53 CST 2022
     */
    private String fileName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column experiment_data.url
     *
     * @mbg.generated Fri Nov 18 13:53:53 CST 2022
     */
    private String url;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column experiment_data.create_user
     *
     * @mbg.generated Fri Nov 18 13:53:53 CST 2022
     */
    private String createUser;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column experiment_data.create_time
     *
     * @mbg.generated Fri Nov 18 13:53:53 CST 2022
     */
    private Date createTime;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column experiment_data.remark
     *
     * @mbg.generated Fri Nov 18 13:53:53 CST 2022
     */
    private String remark;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column experiment_data.id
     *
     * @return the value of experiment_data.id
     *
     * @mbg.generated Fri Nov 18 13:53:53 CST 2022
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column experiment_data.id
     *
     * @param id the value for experiment_data.id
     *
     * @mbg.generated Fri Nov 18 13:53:53 CST 2022
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column experiment_data.file_name
     *
     * @return the value of experiment_data.file_name
     *
     * @mbg.generated Fri Nov 18 13:53:53 CST 2022
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column experiment_data.file_name
     *
     * @param fileName the value for experiment_data.file_name
     *
     * @mbg.generated Fri Nov 18 13:53:53 CST 2022
     */
    public void setFileName(String fileName) {
        this.fileName = fileName == null ? null : fileName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column experiment_data.url
     *
     * @return the value of experiment_data.url
     *
     * @mbg.generated Fri Nov 18 13:53:53 CST 2022
     */
    public String getUrl() {
        return url;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column experiment_data.url
     *
     * @param url the value for experiment_data.url
     *
     * @mbg.generated Fri Nov 18 13:53:53 CST 2022
     */
    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column experiment_data.create_user
     *
     * @return the value of experiment_data.create_user
     *
     * @mbg.generated Fri Nov 18 13:53:53 CST 2022
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column experiment_data.create_user
     *
     * @param createUser the value for experiment_data.create_user
     *
     * @mbg.generated Fri Nov 18 13:53:53 CST 2022
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column experiment_data.create_time
     *
     * @return the value of experiment_data.create_time
     *
     * @mbg.generated Fri Nov 18 13:53:53 CST 2022
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column experiment_data.create_time
     *
     * @param createTime the value for experiment_data.create_time
     *
     * @mbg.generated Fri Nov 18 13:53:53 CST 2022
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column experiment_data.remark
     *
     * @return the value of experiment_data.remark
     *
     * @mbg.generated Fri Nov 18 13:53:53 CST 2022
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column experiment_data.remark
     *
     * @param remark the value for experiment_data.remark
     *
     * @mbg.generated Fri Nov 18 13:53:53 CST 2022
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}