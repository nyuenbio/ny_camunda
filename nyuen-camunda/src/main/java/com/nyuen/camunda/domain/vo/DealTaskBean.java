package com.nyuen.camunda.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;
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

    private String procInstId;

    @ApiModelProperty(value = "流程处理意见")
    private String comment;

    @ApiModelProperty(value = "节点变量")
    private Map<String,Object> variables;

    private List<AttachmentVo> attachmentVoList;

}
