package com.nyuen.camunda.domain.vo;

import lombok.Data;

import java.util.Set;

/**
 * TODO
 *
 * @author chengjl
 * @description 导入样本库实体类
 * @date 2023/4/10
 */
@Data
public class ImportSampleStorageVo {
    private String sampleNum;
    private String sampleLocation;
    private String sampleTypeName;
    private String sampleType;

    private Set<String> sampleNumSet;
    private Set<String> sampleLocationSet;
    private int levelNo;
    private String boxNo;

}
