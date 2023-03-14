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

    //查询某个盒子中的样本
    private String fridgeNo;
    private Integer levelNo;
    private String boxNo;

}
