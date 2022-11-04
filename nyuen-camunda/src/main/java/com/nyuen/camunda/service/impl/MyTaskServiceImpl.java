package com.nyuen.camunda.service.impl;

import com.nyuen.camunda.common.PageBean;
import com.nyuen.camunda.domain.po.ActHiProcinst;
import com.nyuen.camunda.domain.vo.HistoryTask;
import com.nyuen.camunda.domain.vo.TodoTask;
import com.nyuen.camunda.mapper.ActHiProcinstMapper;
import com.nyuen.camunda.mapper.ActHiTaskinstMapper;
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
    @Resource
    private ActHiTaskinstMapper actHiTaskinstMapper;
    @Resource
    private ActHiProcinstMapper actHiProcinstMapper;

    @Override
    public PageBean getTodoTaskList(Map<String, Object> params) {
        List<TodoTask> todoTaskList = actRuTaskMapper.getTodoTaskList(params);
        int total = actRuTaskMapper.getTodoTaskTotal(params);
        return new PageBean(total, todoTaskList);
    }

    @Override
    public PageBean getHistoryTaskList(Map<String, Object> params) {
        List<HistoryTask> historyTaskList = actHiTaskinstMapper.getHistoryTaskList(params);
        int total = actHiTaskinstMapper.getHistoryTaskTotal(params);
        return new PageBean(total, historyTaskList);
    }

    @Override
    public PageBean getSampleProcessList(Map<String,Object> params) {
        List<ActHiProcinst> actHiProcinstList = actHiProcinstMapper.getSampleProcessList(params);
        int total = actHiProcinstMapper.getSampleProcessListCount(params);

        return new PageBean(total, actHiProcinstList);
    }

    @Override
    public List<TodoTask> getTodoTaskByCondition(Map<String, Object> params) {
        return actRuTaskMapper.getTodoTaskByCondition(params);
    }


}
