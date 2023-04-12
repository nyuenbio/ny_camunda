package com.nyuen.camunda.service;

import com.nyuen.camunda.common.PageBean;
import com.nyuen.camunda.domain.po.SampleStorage;
import com.nyuen.camunda.domain.vo.ImportSampleStorageVo;
import com.nyuen.camunda.domain.vo.SampleStoreOperateVo;

import java.util.List;
import java.util.Map;

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

    PageBean getSampleStorageList(Map<String,Object> params);

    List<SampleStorage> getLevelUsedBox(SampleStorage sampleStorage);

    List<SampleStorage> getSampleStorageListWithoutPage(Map<String,Object> params);

    List<SampleStorage> getInfoByLocation(String sampleLocation);

    List<SampleStorage> getBySampleNumOrLocation(ImportSampleStorageVo importSampleStorageVo);


}
