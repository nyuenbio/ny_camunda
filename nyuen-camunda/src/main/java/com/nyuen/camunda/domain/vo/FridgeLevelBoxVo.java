package com.nyuen.camunda.domain.vo;

import lombok.Data;

/**
 * TODO
 *
 * @author chengjl
 * @description 创建新的冰箱-层级-盒子实体类
 * @date 2023/4/6
 */
@Data
public class FridgeLevelBoxVo {
    private String fridgeNo;
    private int levelNo;
    private String sampleType;
    private int boxNum;
}
