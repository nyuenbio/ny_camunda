package com.nyuen.camunda.service;

import com.nyuen.camunda.domain.vo.TodoTask;

import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2022/9/6
 */
public interface MyTaskService {
    List<TodoTask> getTodoTaskList(Map<String,Object> params);
}
