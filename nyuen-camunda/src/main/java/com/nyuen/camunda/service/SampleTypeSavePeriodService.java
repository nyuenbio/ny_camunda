package com.nyuen.camunda.service;

import com.nyuen.camunda.domain.po.SampleTypeSavePeriod;

import java.util.List;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2023/2/21
 */
public interface SampleTypeSavePeriodService {

    Integer getPeriodBySampleType(String sampleType);

    List<SampleTypeSavePeriod> getSavePeriodList();

    void delSavePeriod(int id);

    void updateSavePeriod(SampleTypeSavePeriod sampleTypeSavePeriod);

    void addSavePeriod(SampleTypeSavePeriod sampleTypeSavePeriod);
}
