package com.nyuen.camunda.mapper;

import com.nyuen.camunda.domain.po.ActIdTenant;

public interface ActIdTenantMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_id_tenant
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_id_tenant
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    int insert(ActIdTenant record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_id_tenant
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    int insertSelective(ActIdTenant record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_id_tenant
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    ActIdTenant selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_id_tenant
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    int updateByPrimaryKeySelective(ActIdTenant record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_id_tenant
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    int updateByPrimaryKey(ActIdTenant record);
}