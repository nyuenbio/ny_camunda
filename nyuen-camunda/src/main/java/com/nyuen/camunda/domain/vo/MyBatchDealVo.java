package com.nyuen.camunda.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2023/8/9
 */
@Data
public class MyBatchDealVo {
    private List<String> sampleNumList;
    private String assignee;
    private String procDefId;
    private String nodeName;

}
