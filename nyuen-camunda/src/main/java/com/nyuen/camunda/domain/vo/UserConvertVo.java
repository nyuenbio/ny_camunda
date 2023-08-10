package com.nyuen.camunda.domain.vo;

import lombok.Data;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2023/2/27
 */
@Data
public class UserConvertVo {
    private long loginUserId;
    private String loginUserIdStr;
    private String loginUserName;

    public UserConvertVo(long loginUserId,String loginUserIdStr,String loginUserName){
        this.loginUserId=loginUserId;
        this.loginUserIdStr=loginUserIdStr;
        this.loginUserName=loginUserName;
    }
}
