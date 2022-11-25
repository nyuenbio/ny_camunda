package com.nyuen.camunda.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author chengjl
 * @description start process bean
 * @date 2022/7/28
 */
@Data
public class BatchStartProcessBean implements Serializable {
    @NotEmpty
    private String procDefId;

    private List<String> businessKeyList;

    private List<SampleReceiveBean> sampleReceiveList;

    @NotEmpty
    @ApiModelProperty(value = "发起人id")
    private String initiator;

    private Map<String, Object> variables;

}
