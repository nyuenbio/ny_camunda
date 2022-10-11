package com.nyuen.camunda.domain.vo;

import lombok.Data;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2022/10/11
 */
@Data
public class SimpleQueryBean {

    private String assignee;
    private int currentPage;
    private int pageSize;

}
