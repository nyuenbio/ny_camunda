package com.nyuen.camunda.service.impl;

import com.nyuen.camunda.domain.po.SampleStorageOperation;
import com.nyuen.camunda.mapper.SampleStorageOperationMapper;
import com.nyuen.camunda.service.SampleStorageOperationService;
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
public class SampleStorageOperationServiceImpl implements SampleStorageOperationService {
    @Resource
    private SampleStorageOperationMapper sampleStorageOperationMapper;


    @Override
    public void addSampleStorageOperation(SampleStorageOperation sampleStorageOperation) {
        sampleStorageOperationMapper.insertSelective(sampleStorageOperation);
    }

    @Override
    public List<SampleStorageOperation> getSampleStorageOperation(String sampleNum) {
        return sampleStorageOperationMapper.getSampleStorageOperation(sampleNum);
    }
}
