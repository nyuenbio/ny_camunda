package com.nyuen.camunda.domain.vo;

import com.nyuen.camunda.domain.po.ActRuTask;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2022/9/6
 */
@Data
public class TodoTask implements Serializable {
    @ApiModelProperty(value = "样本编号")
    private String businessKey;
    @ApiModelProperty(value = "流程名称")
    private String procDefName;
    private String id;
    private Integer rev;
    private String executionId;
    private String procInstId;
    private String procDefId;
    private String caseExecutionId;
    private String caseInstId;
    private String caseDefId;
    @ApiModelProperty(value = "节点名称")
    private String name;
    private String parentTaskId;
    private String description;
    @ApiModelProperty(value = "activity_key")
    private String taskDefKey;
    private String owner;
    @ApiModelProperty(value = "待办人id")
    private String assignee;
    private String delegation;
    private Integer priority;
    private Date createTime;
    private Date dueDate;
    private Date followUpDate;
    private Integer suspensionState;
    private String tenantId;

}
