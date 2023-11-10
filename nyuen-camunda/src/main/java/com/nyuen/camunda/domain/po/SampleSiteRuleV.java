package com.nyuen.camunda.domain.po;

import java.util.Date;

public class SampleSiteRuleV {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sample_site_rule_v.id
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    private Integer id;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sample_site_rule_v.product_name_abbr
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    private String productNameAbbr;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sample_site_rule_v.test_type
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    private String testType;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sample_site_rule_v.product_name
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    private String productName;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sample_site_rule_v.medicine_num
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    private Integer medicineNum;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sample_site_rule_v.hole_num
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    private Integer holeNum;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sample_site_rule_v.hole_code
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    private String holeCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sample_site_rule_v.assay_code
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    private String assayCode;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sample_site_rule_v.state
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    private Integer state;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sample_site_rule_v.medicine
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    private String medicine;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sample_site_rule_v.hla_state
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    private Integer hlaState;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sample_site_rule_v.remark
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    private String remark;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sample_site_rule_v.create_user
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    private String createUser;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column sample_site_rule_v.create_time
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    private Date createTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sample_site_rule_v.id
     *
     * @return the value of sample_site_rule_v.id
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    public Integer getId() {
        return id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sample_site_rule_v.id
     *
     * @param id the value for sample_site_rule_v.id
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sample_site_rule_v.product_name_abbr
     *
     * @return the value of sample_site_rule_v.product_name_abbr
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    public String getProductNameAbbr() {
        return productNameAbbr;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sample_site_rule_v.product_name_abbr
     *
     * @param productNameAbbr the value for sample_site_rule_v.product_name_abbr
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    public void setProductNameAbbr(String productNameAbbr) {
        this.productNameAbbr = productNameAbbr == null ? null : productNameAbbr.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sample_site_rule_v.test_type
     *
     * @return the value of sample_site_rule_v.test_type
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    public String getTestType() {
        return testType;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sample_site_rule_v.test_type
     *
     * @param testType the value for sample_site_rule_v.test_type
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    public void setTestType(String testType) {
        this.testType = testType == null ? null : testType.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sample_site_rule_v.product_name
     *
     * @return the value of sample_site_rule_v.product_name
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    public String getProductName() {
        return productName;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sample_site_rule_v.product_name
     *
     * @param productName the value for sample_site_rule_v.product_name
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    public void setProductName(String productName) {
        this.productName = productName == null ? null : productName.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sample_site_rule_v.medicine_num
     *
     * @return the value of sample_site_rule_v.medicine_num
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    public Integer getMedicineNum() {
        return medicineNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sample_site_rule_v.medicine_num
     *
     * @param medicineNum the value for sample_site_rule_v.medicine_num
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    public void setMedicineNum(Integer medicineNum) {
        this.medicineNum = medicineNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sample_site_rule_v.hole_num
     *
     * @return the value of sample_site_rule_v.hole_num
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    public Integer getHoleNum() {
        return holeNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sample_site_rule_v.hole_num
     *
     * @param holeNum the value for sample_site_rule_v.hole_num
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    public void setHoleNum(Integer holeNum) {
        this.holeNum = holeNum;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sample_site_rule_v.hole_code
     *
     * @return the value of sample_site_rule_v.hole_code
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    public String getHoleCode() {
        return holeCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sample_site_rule_v.hole_code
     *
     * @param holeCode the value for sample_site_rule_v.hole_code
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    public void setHoleCode(String holeCode) {
        this.holeCode = holeCode == null ? null : holeCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sample_site_rule_v.assay_code
     *
     * @return the value of sample_site_rule_v.assay_code
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    public String getAssayCode() {
        return assayCode;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sample_site_rule_v.assay_code
     *
     * @param assayCode the value for sample_site_rule_v.assay_code
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    public void setAssayCode(String assayCode) {
        this.assayCode = assayCode == null ? null : assayCode.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sample_site_rule_v.state
     *
     * @return the value of sample_site_rule_v.state
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    public Integer getState() {
        return state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sample_site_rule_v.state
     *
     * @param state the value for sample_site_rule_v.state
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sample_site_rule_v.medicine
     *
     * @return the value of sample_site_rule_v.medicine
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    public String getMedicine() {
        return medicine;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sample_site_rule_v.medicine
     *
     * @param medicine the value for sample_site_rule_v.medicine
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    public void setMedicine(String medicine) {
        this.medicine = medicine == null ? null : medicine.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sample_site_rule_v.hla_state
     *
     * @return the value of sample_site_rule_v.hla_state
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    public Integer getHlaState() {
        return hlaState;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sample_site_rule_v.hla_state
     *
     * @param hlaState the value for sample_site_rule_v.hla_state
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    public void setHlaState(Integer hlaState) {
        this.hlaState = hlaState;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sample_site_rule_v.remark
     *
     * @return the value of sample_site_rule_v.remark
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    public String getRemark() {
        return remark;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sample_site_rule_v.remark
     *
     * @param remark the value for sample_site_rule_v.remark
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sample_site_rule_v.create_user
     *
     * @return the value of sample_site_rule_v.create_user
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    public String getCreateUser() {
        return createUser;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sample_site_rule_v.create_user
     *
     * @param createUser the value for sample_site_rule_v.create_user
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    public void setCreateUser(String createUser) {
        this.createUser = createUser == null ? null : createUser.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column sample_site_rule_v.create_time
     *
     * @return the value of sample_site_rule_v.create_time
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column sample_site_rule_v.create_time
     *
     * @param createTime the value for sample_site_rule_v.create_time
     *
     * @mbg.generated Thu Nov 09 10:26:47 CST 2023
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
