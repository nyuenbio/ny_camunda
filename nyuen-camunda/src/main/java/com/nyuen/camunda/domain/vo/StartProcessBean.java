package com.nyuen.camunda.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotEmpty;
import java.util.Map;

/**
 * TODO
 *
 * @author chengjl
 * @description start process bean
 * @date 2022/7/28
 */
@Data
public class StartProcessBean {
    @NotEmpty
    private String procDefId;

    private String businessKey;

    @NotEmpty
    @ApiModelProperty(value = "发起人id")
    private String initiator;

    private Map<String, Object> variables;

}
