package com.nyuen.camunda.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2022/11/4
 */
@Data
public class SampleRow {
    private String sampleInfo;
    private List<String> rowData;

    public SampleRow(String sampleInfo, List<String> rowData){
        this.sampleInfo = sampleInfo;
        this.rowData = rowData;
    }
}
