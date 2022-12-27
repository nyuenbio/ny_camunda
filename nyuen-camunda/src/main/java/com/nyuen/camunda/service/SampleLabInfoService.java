package com.nyuen.camunda.service;

import com.nyuen.camunda.domain.po.SampleLabInfo;

import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2022/11/29
 */
public interface SampleLabInfoService {

    void addSampleLanInfo(SampleLabInfo sampleLabInfo);

    List<SampleLabInfo> getSampleLabInfoList(List<String> procInstIdList);

    SampleLabInfo getLastSampleLabInfoBySampleNum(String sampleInfo);
}
