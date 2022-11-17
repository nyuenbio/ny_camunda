package com.nyuen.camunda.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2022/8/17
 */
@Data
public class FormBean  implements Serializable {
    @NotEmpty
    @ApiModelProperty(value = "表单id")
    private String formId;

    @NotEmpty
    @ApiModelProperty(value = "表单文件内容")
    private Object formText;
}
