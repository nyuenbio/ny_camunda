package com.nyuen.camunda.mapper;

import com.nyuen.camunda.domain.po.SampleStorage;
import com.nyuen.camunda.domain.vo.SampleStoreOperateVo;

import java.util.List;
import java.util.Map;

public interface SampleStorageMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sample_storage
     *
     * @mbg.generated Mon Feb 27 13:38:18 CST 2023
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sample_storage
     *
     * @mbg.generated Mon Feb 27 13:38:18 CST 2023
     */
    int insert(SampleStorage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sample_storage
     *
     * @mbg.generated Mon Feb 27 13:38:18 CST 2023
     */
    int insertSelective(SampleStorage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sample_storage
     *
     * @mbg.generated Mon Feb 27 13:38:18 CST 2023
     */
    SampleStorage selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sample_storage
     *
     * @mbg.generated Mon Feb 27 13:38:18 CST 2023
     */
    int updateByPrimaryKeySelective(SampleStorage record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table sample_storage
     *
     * @mbg.generated Mon Feb 27 13:38:18 CST 2023
     */
    int updateByPrimaryKey(SampleStorage record);

    /**
     *
     * 自定义部分
     *
     */

    SampleStorage getLastSampleLocation(SampleStorage sampleStorage);

    void sampleStoreOperate(SampleStoreOperateVo sampleStoreOperateVo);

    List<SampleStorage> getSampleStorageList(Map<String,Object> params);

    int getSampleStorageTotal(Map<String,Object> params);
}