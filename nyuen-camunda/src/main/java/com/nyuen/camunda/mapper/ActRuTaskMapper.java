package com.nyuen.camunda.mapper;

import com.nyuen.camunda.domain.po.ActRuTask;
import com.nyuen.camunda.domain.vo.TodoTask;

import java.util.List;
import java.util.Map;

public interface ActRuTaskMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_ru_task
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_ru_task
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    int insert(ActRuTask record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_ru_task
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    int insertSelective(ActRuTask record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_ru_task
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    ActRuTask selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_ru_task
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    int updateByPrimaryKeySelective(ActRuTask record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table act_ru_task
     *
     * @mbg.generated Tue Sep 06 10:38:43 CST 2022
     */
    int updateByPrimaryKey(ActRuTask record);

    /**
     *
     * 自定义部分
     */

    List<TodoTask> getTodoTaskList(Map<String,Object> params);
    int getTodoTaskTotal(Map<String,Object> params);

    List<TodoTask> getTodoTaskByCondition(Map<String,Object> params);

}
