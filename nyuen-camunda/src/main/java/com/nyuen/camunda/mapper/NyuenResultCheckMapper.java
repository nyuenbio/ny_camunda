package com.nyuen.camunda.mapper;

import com.nyuen.camunda.domain.po.NyuenResultCheck;
import com.nyuen.camunda.domain.vo.SampleRowAndCell;

import java.util.List;

public interface NyuenResultCheckMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table nyuen_result_check
     *
     * @mbg.generated Mon Dec 26 16:47:42 CST 2022
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table nyuen_result_check
     *
     * @mbg.generated Mon Dec 26 16:47:42 CST 2022
     */
    int insert(NyuenResultCheck record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table nyuen_result_check
     *
     * @mbg.generated Mon Dec 26 16:47:42 CST 2022
     */
    int insertSelective(NyuenResultCheck record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table nyuen_result_check
     *
     * @mbg.generated Mon Dec 26 16:47:42 CST 2022
     */
    NyuenResultCheck selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table nyuen_result_check
     *
     * @mbg.generated Mon Dec 26 16:47:42 CST 2022
     */
    int updateByPrimaryKeySelective(NyuenResultCheck record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table nyuen_result_check
     *
     * @mbg.generated Mon Dec 26 16:47:42 CST 2022
     */
    int updateByPrimaryKey(NyuenResultCheck record);

    /**
     *
     * 自定义部分
     */

    void delResultBySampleInfo(List<SampleRowAndCell> list);

    void updateSnpInfo(List<SampleRowAndCell> list);

    List<NyuenResultCheck> getSnpInfoBySampleNums(List<SampleRowAndCell> list);

    List<NyuenResultCheck> getCallResultErrorBySampleNums(List<NyuenResultCheck> list);

}
