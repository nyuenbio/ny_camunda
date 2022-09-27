package com.nyuen.camunda.service.impl;

import com.nyuen.camunda.domain.vo.TodoTask;
import com.nyuen.camunda.mapper.ActRuTaskMapper;
import com.nyuen.camunda.service.MyTaskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2022/9/6
 */
@Service
public class MyTaskServiceImpl implements MyTaskService {
    @Resource
    private ActRuTaskMapper actRuTaskMapper;

    @Override
    public List<TodoTask> getTodoTaskList(Map<String, Object> params) {
        return actRuTaskMapper.getTodoTaskList(params);
    }
}
