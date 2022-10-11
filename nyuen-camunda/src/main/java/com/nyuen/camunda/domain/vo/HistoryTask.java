package com.nyuen.camunda.domain.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 自定义我的已办任务实体类
 *
 * @author chengjl
 * @description
 * @date 2022/10/11
 */
@Data
public class HistoryTask implements Serializable {
    private String businessKey;
    private String defName;

    private String id;

    private String taskDefKey;

    private String procDefKey;

    private String procDefId;

    private String rootProcInstId;

    private String procInstId;

    private String executionId;

    private String caseDefKey;

    private String caseDefId;

    private String caseInstId;

    private String caseExecutionId;

    private String actInstId;

    private String name;

    private String parentTaskId;

    private String description;

    private String owner;

    private String assignee;

    private Date startTime;

    private Date endTime;

    private Long duration;

    private String deleteReason;

    private Integer priority;

    private Date dueDate;

    private Date followUpDate;

    private String tenantId;

    private Date removalTime;

}
