package com.nyuen.camunda.mapper;

import com.nyuen.camunda.domain.po.LabFridgeLevel;
import com.nyuen.camunda.domain.vo.ImportSampleStorageVo;
import com.nyuen.camunda.domain.vo.SampleStorageVo;

import java.util.List;
import java.util.Set;

public interface LabFridgeLevelMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lab_fridge_level
     *
     * @mbg.generated Thu Apr 06 14:19:05 CST 2023
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lab_fridge_level
     *
     * @mbg.generated Thu Apr 06 14:19:05 CST 2023
     */
    int insert(LabFridgeLevel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lab_fridge_level
     *
     * @mbg.generated Thu Apr 06 14:19:05 CST 2023
     */
    int insertSelective(LabFridgeLevel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lab_fridge_level
     *
     * @mbg.generated Thu Apr 06 14:19:05 CST 2023
     */
    LabFridgeLevel selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lab_fridge_level
     *
     * @mbg.generated Thu Apr 06 14:19:05 CST 2023
     */
    int updateByPrimaryKeySelective(LabFridgeLevel record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lab_fridge_level
     *
     * @mbg.generated Thu Apr 06 14:19:05 CST 2023
     */
    int updateByPrimaryKey(LabFridgeLevel record);



    /**
     *
     * 自定义部分
     */
    List<LabFridgeLevel> getInfoByFridgeNoAndLevel(com.nyuen.camunda.domain.po.LabFridgeLevel labFridgeLevel);

    List<com.nyuen.camunda.domain.po.LabFridgeLevel> getFridgeUsedLevel(String fridgeNo);

    //获取所有的层级-盒子编号
    Set<String> getAllFridgeLevelBoxNo();

    //获取冰箱所有层级、样本类型及每个层级的盒子数量
    List<SampleStorageVo> getFridgeLevelAndBoxCount(String fridgeNo);
}
