package com.nyuen.camunda.mapper;

import com.nyuen.camunda.domain.po.ActHiDetail;

public interface ActHiDetailMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_hi_detail
     *
     * @mbg.generated Tue Sep 06 11:07:10 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_hi_detail
     *
     * @mbg.generated Tue Sep 06 11:07:10 CST 2022
     */
    int insert(ActHiDetail record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_hi_detail
     *
     * @mbg.generated Tue Sep 06 11:07:10 CST 2022
     */
    int insertSelective(ActHiDetail record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_hi_detail
     *
     * @mbg.generated Tue Sep 06 11:07:10 CST 2022
     */
    ActHiDetail selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_hi_detail
     *
     * @mbg.generated Tue Sep 06 11:07:10 CST 2022
     */
    int updateByPrimaryKeySelective(ActHiDetail record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_hi_detail
     *
     * @mbg.generated Tue Sep 06 11:07:10 CST 2022
     */
    int updateByPrimaryKey(ActHiDetail record);
}