package com.nyuen.camunda.domain.po;

public class ActIdMembership {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column act_id_membership.USER_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    private String userId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column act_id_membership.GROUP_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    private String groupId;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column act_id_membership.USER_ID_
     *
     * @return the value of act_id_membership.USER_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public String getUserId() {
        return userId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column act_id_membership.USER_ID_
     *
     * @param userId the value for act_id_membership.USER_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column act_id_membership.GROUP_ID_
     *
     * @return the value of act_id_membership.GROUP_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column act_id_membership.GROUP_ID_
     *
     * @param groupId the value for act_id_membership.GROUP_ID_
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    public void setGroupId(String groupId) {
        this.groupId = groupId == null ? null : groupId.trim();
    }
}