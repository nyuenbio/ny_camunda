package com.nyuen.camunda.service;

import com.nyuen.camunda.common.PageBean;
import com.nyuen.camunda.domain.vo.TodoTask;
import javafx.print.PageRange;

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
    PageBean getTodoTaskList(Map<String,Object> params);

    PageBean getHistoryTaskList(Map<String,Object> params);
}
