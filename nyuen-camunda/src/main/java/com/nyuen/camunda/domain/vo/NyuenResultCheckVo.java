package com.nyuen.camunda.domain.vo;

import com.nyuen.camunda.common.BaseBean;
import lombok.Data;

import java.util.Date;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2023/3/1
 */
@Data
public class NyuenResultCheckVo extends BaseBean {
    private Integer id;

    private String sampleInfo;

    private String assayId;

    private String assayType;

    private String snp;

    private String callResult;

    private Long createUserId;

    private String createUser;

    private Date createTime;

    private Date createTimeStart;

    private Date createTimeEnd;
}
