package com.nyuen.camunda.mapper;

import com.nyuen.camunda.domain.po.ActIdTenantMember;

public interface ActIdTenantMemberMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_id_tenant_member
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_id_tenant_member
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    int insert(ActIdTenantMember record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_id_tenant_member
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    int insertSelective(ActIdTenantMember record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_id_tenant_member
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    ActIdTenantMember selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_id_tenant_member
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    int updateByPrimaryKeySelective(ActIdTenantMember record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_id_tenant_member
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    int updateByPrimaryKey(ActIdTenantMember record);
}