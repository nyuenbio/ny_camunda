package com.nyuen.camunda.service;

import com.nyuen.camunda.common.PageBean;
import com.nyuen.camunda.domain.po.ExperimentData;

import java.util.Map;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2022/11/18
 */
public interface ExperimentDataService {

    void addExperimentData(ExperimentData experimentData);

    PageBean getExperimentDataList(Map<String,Object> params);

    void updateExperimentData(ExperimentData experimentData);

}
