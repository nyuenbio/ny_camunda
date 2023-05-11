package com.nyuen.camunda.domain.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 收样实体类
 *
 * @author chengjl
 * @description
 * @date 2022/11/21
 */
@Data
public class SampleReceiveBean implements Serializable {
    private String sampleInfo;
    private int productType;
    private String productName;
    private String sampleType;//样本类型
    private int hospitalId;//医院id
    private String hospitalName;//医院名称

    private String testType;//检测类型:全面药物检测,常用药物检测,自选药物检测
    private String medicines;//药物名称

}
