package com.nyuen.camunda.service.impl;

import com.nyuen.camunda.mapper.SampleTypeSavePeriodMapper;
import com.nyuen.camunda.service.SampleTypeSavePeriodService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2023/2/21
 */
@Service
public class SampleTypeSavePeriodServiceImpl implements SampleTypeSavePeriodService {
    @Resource
    private SampleTypeSavePeriodMapper sampleTypeSavePeriodMapper;


    @Override
    public Integer getPeriodBySampleType(String sampleType) {
        return sampleTypeSavePeriodMapper.getPeriodBySampleType(sampleType);
    }
}
