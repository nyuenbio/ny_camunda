package com.nyuen.camunda.domain.po;

import java.util.Date;

public class NyuenIdV {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column nyuen_id_v.nyuen_id
     *
     * @mbg.generated Tue Nov 07 15:47:08 CST 2023
     */
    private String nyuenId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column nyuen_id_v.snp
     *
     * @mbg.generated Tue Nov 07 15:47:08 CST 2023
     */
    private String snp;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column nyuen_id_v.well
     *
     * @mbg.generated Tue Nov 07 15:47:08 CST 2023
     */
    private String well;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column nyuen_id_v.create_time
     *
     * @mbg.generated Tue Nov 07 15:47:08 CST 2023
     */
    private Date createTime;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column nyuen_id_v.nyuen_id
     *
     * @return the value of nyuen_id_v.nyuen_id
     *
     * @mbg.generated Tue Nov 07 15:47:08 CST 2023
     */
    public String getNyuenId() {
        return nyuenId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column nyuen_id_v.nyuen_id
     *
     * @param nyuenId the value for nyuen_id_v.nyuen_id
     *
     * @mbg.generated Tue Nov 07 15:47:08 CST 2023
     */
    public void setNyuenId(String nyuenId) {
        this.nyuenId = nyuenId == null ? null : nyuenId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column nyuen_id_v.snp
     *
     * @return the value of nyuen_id_v.snp
     *
     * @mbg.generated Tue Nov 07 15:47:08 CST 2023
     */
    public String getSnp() {
        return snp;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column nyuen_id_v.snp
     *
     * @param snp the value for nyuen_id_v.snp
     *
     * @mbg.generated Tue Nov 07 15:47:08 CST 2023
     */
    public void setSnp(String snp) {
        this.snp = snp == null ? null : snp.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column nyuen_id_v.well
     *
     * @return the value of nyuen_id_v.well
     *
     * @mbg.generated Tue Nov 07 15:47:08 CST 2023
     */
    public String getWell() {
        return well;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column nyuen_id_v.well
     *
     * @param well the value for nyuen_id_v.well
     *
     * @mbg.generated Tue Nov 07 15:47:08 CST 2023
     */
    public void setWell(String well) {
        this.well = well == null ? null : well.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column nyuen_id_v.create_time
     *
     * @return the value of nyuen_id_v.create_time
     *
     * @mbg.generated Tue Nov 07 15:47:08 CST 2023
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column nyuen_id_v.create_time
     *
     * @param createTime the value for nyuen_id_v.create_time
     *
     * @mbg.generated Tue Nov 07 15:47:08 CST 2023
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
