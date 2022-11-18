package com.nyuen.camunda.service.impl;

import com.nyuen.camunda.domain.po.ExperimentData;
import com.nyuen.camunda.mapper.ExperimentDataMapper;
import com.nyuen.camunda.service.ExperimentDataService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

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
}
