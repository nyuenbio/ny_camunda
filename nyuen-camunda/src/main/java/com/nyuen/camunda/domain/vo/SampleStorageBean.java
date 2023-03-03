package com.nyuen.camunda.domain.vo;

import com.nyuen.camunda.common.BaseBean;
import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2023/3/1
 */
@Data
public class SampleStorageBean {
    private Integer currentPage;
    private Integer pageSize;
    private List<String> sampleNumList;
    private int sampleStorageState;

}
