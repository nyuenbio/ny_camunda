package com.nyuen.camunda.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.Map;

/**
 * TODO
 *
 * @author chengjl
 * @description deal task bean
 * @date 2022/7/28
 */
@Data
public class DealTaskBean {
    @NotEmpty
    @ApiModelProperty(value = "办理人")
    private String assignee;

    @NotEmpty
    private String taskId;
    @NotEmpty
    private String processInstanceId;
//    private String activityInstanceId;
//    private String executionId;

    @ApiModelProperty(value = "流程处理意见")
    private String comment;

    @ApiModelProperty(value = "节点变量")
    private Map<String,Object> variables;

//    @ApiModelProperty(value = "表单变量")
//    private Map<String, Object> properties;
}
