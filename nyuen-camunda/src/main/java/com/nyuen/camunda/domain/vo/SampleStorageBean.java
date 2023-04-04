package com.nyuen.camunda.domain.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 样本库信息查询bean
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
    private String fridgeNo;//冰箱编号
    private Integer levelNo;//冰箱层级
    private String boxNo;//盒子编号

    private Date createTimeStart;//入库时间起
    private Date createTimeEnd;//入库时间止
}
