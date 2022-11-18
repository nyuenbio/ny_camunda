package com.nyuen.camunda.domain.vo;

import lombok.Data;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2022/11/18
 */
@Data
public class ExtractRow {
    private String sampleNum;
    private String qubit;//浓度
    private String volume;
    private String totalWeight;

}
