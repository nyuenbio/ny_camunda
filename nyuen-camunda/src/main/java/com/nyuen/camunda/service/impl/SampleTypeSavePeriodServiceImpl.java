package com.nyuen.camunda.service.impl;

import com.nyuen.camunda.domain.po.SampleTypeSavePeriod;
import com.nyuen.camunda.mapper.SampleTypeSavePeriodMapper;
import com.nyuen.camunda.service.SampleTypeSavePeriodService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

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

    @Override
    public List<SampleTypeSavePeriod> getSavePeriodList() {
        return sampleTypeSavePeriodMapper.getSavePeriodList();
    }

    @Override
    public void delSavePeriod(int id) {
        sampleTypeSavePeriodMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void updateSavePeriod(SampleTypeSavePeriod sampleTypeSavePeriod) {
        sampleTypeSavePeriodMapper.updateByPrimaryKeySelective(sampleTypeSavePeriod);
    }

    @Override
    public void addSavePeriod(SampleTypeSavePeriod sampleTypeSavePeriod) {
        sampleTypeSavePeriodMapper.insertSelective(sampleTypeSavePeriod);
    }
}
