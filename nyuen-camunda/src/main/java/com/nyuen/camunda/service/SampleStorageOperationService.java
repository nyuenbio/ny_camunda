package com.nyuen.camunda.service;

import com.nyuen.camunda.domain.po.SampleStorageOperation;

import java.util.List;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2023/2/21
 */
public interface SampleStorageOperationService {

    void addSampleStorageOperation(SampleStorageOperation sampleStorageOperation);

    List<SampleStorageOperation> getSampleStorageOperation(String sampleNum);
}
