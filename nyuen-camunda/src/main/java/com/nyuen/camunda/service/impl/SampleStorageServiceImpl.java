package com.nyuen.camunda.service.impl;

import com.nyuen.camunda.domain.po.SampleStorage;
import com.nyuen.camunda.domain.vo.SampleStoreOperateVo;
import com.nyuen.camunda.mapper.SampleStorageMapper;
import com.nyuen.camunda.service.SampleStorageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 样本回收储存
 *
 * @author chengjl
 * @description
 * @date 2023/2/21
 */
@Service
public class SampleStorageServiceImpl implements SampleStorageService {
    @Resource
    private SampleStorageMapper sampleStorageMapper;

    @Override
    public SampleStorage getLastSampleLocation(SampleStorage sampleStorage) {
        return sampleStorageMapper.getLastSampleLocation(sampleStorage);
    }

    @Override
    public void addSampleStorage(SampleStorage sampleStorage) {
        sampleStorageMapper.insertSelective(sampleStorage);
    }

    @Override
    public void sampleStoreOperate(SampleStoreOperateVo sampleStoreOperateVo) {
        sampleStorageMapper.sampleStoreOperate(sampleStoreOperateVo);
    }
}
