package com.nyuen.camunda.mapper;

import com.nyuen.camunda.domain.po.ActHiProcinst;

public interface ActHiProcinstMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_hi_procinst
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_hi_procinst
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    int insert(ActHiProcinst record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_hi_procinst
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    int insertSelective(ActHiProcinst record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_hi_procinst
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    ActHiProcinst selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_hi_procinst
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    int updateByPrimaryKeySelective(ActHiProcinst record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_hi_procinst
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    int updateByPrimaryKey(ActHiProcinst record);
}