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
public class ExperimentalDataRow {

    private String sampleNum;
    private String well;
    private String genotype;
    private String snpId;
    private String description;
    private String geneHap;
    private String methodology;

}
