package com.nyuen.camunda.service.impl;

import com.nyuen.camunda.common.PageBean;
import com.nyuen.camunda.domain.po.ExperimentData;
import com.nyuen.camunda.mapper.ExperimentDataMapper;
import com.nyuen.camunda.service.ExperimentDataService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2022/11/18
 */
@Service
public class ExperimentDataServiceImpl implements ExperimentDataService {
    @Resource
    private ExperimentDataMapper experimentDataMapper;


    @Override
    public void addExperimentData(ExperimentData experimentData) {
        experimentDataMapper.insertSelective(experimentData);
    }

    @Override
    public PageBean getExperimentDataList(Map<String, Object> params) {
        List<ExperimentData> experimentDataList = experimentDataMapper.getExperimentDataList(params);
        int total = experimentDataMapper.getExperimentDataCount(params);
        return new PageBean(total,experimentDataList);
    }
}
