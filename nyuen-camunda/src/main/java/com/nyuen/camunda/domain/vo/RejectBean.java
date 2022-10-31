package com.nyuen.camunda.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.ws.rs.DefaultValue;

/**
 * 驳回实体类
 *
 * @author chengjl
 * @description
 * @date 2022/10/31
 */
@ApiModel("驳回实体类")
@Data
public class RejectBean {

    @ApiModelProperty(required = true)
    private String processInstanceId;

    @ApiModelProperty(value = "当前登录用户id",required = true)
    private String currentUserId;

    @ApiModelProperty("驳回原因")
    @DefaultValue("")
    private String rejectComment;


}
