package com.nyuen.camunda.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2022/10/19
 */
@Data
public class Membership {
    private String userId;
    private List<String> groupIdList;
}
