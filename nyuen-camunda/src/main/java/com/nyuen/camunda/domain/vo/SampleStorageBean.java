package com.nyuen.camunda.domain.vo;

import com.nyuen.camunda.common.BaseBean;
import lombok.Data;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2023/3/1
 */
@Data
public class SampleStorageBean extends BaseBean {
    private String sampleNum;
    private int sampleStorageState;
}
