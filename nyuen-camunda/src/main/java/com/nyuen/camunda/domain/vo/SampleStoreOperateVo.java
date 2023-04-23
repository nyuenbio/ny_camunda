package com.nyuen.camunda.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * 样本回收操作实体类
 *
 * @author chengjl
 * @description
 * @date 2023/2/27
 */
@Data
public class SampleStoreOperateVo {

    private List<String> sampleNumList;//样本编号列表
    private Integer sampleStorageState;//样本回收状态:入库,领出,归还,返样,销毁,作废,移动
    private String remark;

}
