package com.nyuen.camunda.service.impl;

import com.nyuen.camunda.domain.po.SampleLabInfo;
import com.nyuen.camunda.mapper.SampleLabInfoMapper;
import com.nyuen.camunda.service.SampleLabInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2022/11/29
 */
@Service
public class SampleLabInfoServiceImpl implements SampleLabInfoService {
    @Resource
    private SampleLabInfoMapper sampleLabInfoMapper;


    @Override
    public void addSampleLanInfo(SampleLabInfo sampleLabInfo) {
        sampleLabInfoMapper.insertSelective(sampleLabInfo);
    }

    @Override
    public List<SampleLabInfo> getSampleLabInfoList(List<String> procInstIdList) {
        return sampleLabInfoMapper.getSampleLabInfoList(procInstIdList);
    }
}
