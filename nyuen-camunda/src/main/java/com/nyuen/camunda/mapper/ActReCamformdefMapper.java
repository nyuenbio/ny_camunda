package com.nyuen.camunda.mapper;

import com.nyuen.camunda.domain.po.ActReCamformdef;

public interface ActReCamformdefMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_re_camformdef
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_re_camformdef
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    int insert(ActReCamformdef record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_re_camformdef
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    int insertSelective(ActReCamformdef record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_re_camformdef
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    ActReCamformdef selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_re_camformdef
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    int updateByPrimaryKeySelective(ActReCamformdef record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_re_camformdef
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    int updateByPrimaryKey(ActReCamformdef record);
}