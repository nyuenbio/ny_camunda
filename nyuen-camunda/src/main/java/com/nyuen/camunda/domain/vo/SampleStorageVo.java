package com.nyuen.camunda.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * 样本回收存储实体类
 *
 * @author chengjl
 * @description
 * @date 2023/2/28
 */
@Data
public class SampleStorageVo {
    private String fridgeNo;
    private int levelNo;
    private String sampleType;
    private int boxNum;
    private int fMax;
    private List<String> sampleNumList;
    private String boxNo;
}
