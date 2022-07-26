package com.nyuen.camunda.mapper;

import com.nyuen.camunda.domain.po.ActHiTaskinst;
import com.nyuen.camunda.domain.vo.HistoryTask;

import java.util.List;
import java.util.Map;

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

    /**
     *
     * 自定义部分
     *
     */

    List<HistoryTask> getHistoryTaskList(Map<String,Object> params);

    int getHistoryTaskTotal(Map<String,Object> params);
}
