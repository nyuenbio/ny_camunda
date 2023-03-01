package com.nyuen.camunda.service.impl;

import com.nyuen.camunda.common.PageBean;
import com.nyuen.camunda.domain.po.SampleStorage;
import com.nyuen.camunda.domain.vo.SampleStoreOperateVo;
import com.nyuen.camunda.mapper.SampleStorageMapper;
import com.nyuen.camunda.service.SampleStorageService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

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

    @Override
    public PageBean getSampleStorageList(Map<String, Object> params) {
        List<SampleStorage> sampleStorageList = sampleStorageMapper.getSampleStorageList(params);
        int total = sampleStorageMapper.getSampleStorageTotal(params);
        return new PageBean(total,sampleStorageList);
    }
}
