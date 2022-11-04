package com.nyuen.camunda.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author chengjl
 * @description 二位数组表格
 * @date 2022/11/4
 */
@Data
public class SampleRowAndCell {
    private String sampleInfo;
    private List<List<String>> sampleRowList;

    public SampleRowAndCell(String sampleInfo, List<List<String>> sampleRowList){
        this.sampleInfo = sampleInfo;
        this.sampleRowList = sampleRowList;
    }
}
