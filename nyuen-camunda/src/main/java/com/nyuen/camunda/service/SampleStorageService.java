package com.nyuen.camunda.service;

import com.nyuen.camunda.domain.po.SampleStorage;
import com.nyuen.camunda.domain.vo.SampleStoreOperateVo;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2023/2/21
 */
public interface SampleStorageService {

    SampleStorage getLastSampleLocation(SampleStorage sampleStorage);

    void addSampleStorage(SampleStorage sampleStorage);

    void sampleStoreOperate(SampleStoreOperateVo sampleStoreOperateVo);

}
