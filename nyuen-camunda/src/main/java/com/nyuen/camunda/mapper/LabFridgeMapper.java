package com.nyuen.camunda.mapper;

import com.nyuen.camunda.domain.po.LabFridge;
import com.nyuen.camunda.domain.vo.ImportSampleStorageVo;

import java.util.List;
import java.util.Set;

public interface LabFridgeMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lab_fridge
     *
     * @mbg.generated Mon Feb 20 14:48:00 CST 2023
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lab_fridge
     *
     * @mbg.generated Mon Feb 20 14:48:00 CST 2023
     */
    int insert(LabFridge record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lab_fridge
     *
     * @mbg.generated Mon Feb 20 14:48:00 CST 2023
     */
    int insertSelective(LabFridge record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lab_fridge
     *
     * @mbg.generated Mon Feb 20 14:48:00 CST 2023
     */
    LabFridge selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lab_fridge
     *
     * @mbg.generated Mon Feb 20 14:48:00 CST 2023
     */
    int updateByPrimaryKeySelective(LabFridge record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table lab_fridge
     *
     * @mbg.generated Mon Feb 20 14:48:00 CST 2023
     */
    int updateByPrimaryKey(LabFridge record);


    /**
     *
     * 自定义部分
     *
     */

    List<LabFridge> getLabFridgeByNo(String fridgeNo);

    List<LabFridge> getAllLabFridges();

    //获取所有的冰箱编号
    Set<String> getAllFridgeNo();

}
