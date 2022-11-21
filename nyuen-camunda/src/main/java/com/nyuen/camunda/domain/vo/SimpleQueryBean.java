package com.nyuen.camunda.domain.vo;

import lombok.Data;

import java.util.Date;

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
    private String name;
    private int currentPage;
    private int pageSize;
    private Date startTime;
    private Date endTime;

}
