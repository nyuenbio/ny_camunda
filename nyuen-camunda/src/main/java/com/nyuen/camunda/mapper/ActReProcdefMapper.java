package com.nyuen.camunda.mapper;

import com.nyuen.camunda.domain.po.ActReProcdef;

public interface ActReProcdefMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_re_procdef
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_re_procdef
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    int insert(ActReProcdef record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_re_procdef
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    int insertSelective(ActReProcdef record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_re_procdef
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    ActReProcdef selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_re_procdef
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    int updateByPrimaryKeySelective(ActReProcdef record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_re_procdef
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    int updateByPrimaryKey(ActReProcdef record);
}