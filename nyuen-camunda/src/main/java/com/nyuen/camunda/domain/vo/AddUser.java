package com.nyuen.camunda.domain.vo;

import lombok.Data;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2022/8/2
 */
@Data
public class AddUser {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String pwd;
}
