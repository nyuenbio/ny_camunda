package com.nyuen.camunda.mapper;

import com.nyuen.camunda.domain.po.ActHiTaskinst;

public interface ActHiTaskinstMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_hi_taskinst
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_hi_taskinst
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    int insert(ActHiTaskinst record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_hi_taskinst
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    int insertSelective(ActHiTaskinst record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_hi_taskinst
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    ActHiTaskinst selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_hi_taskinst
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    int updateByPrimaryKeySelective(ActHiTaskinst record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_hi_taskinst
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    int updateByPrimaryKey(ActHiTaskinst record);
}