package com.nyuen.camunda.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * TODO
 *
 * @author chengjl
 * @description deployment bean
 * @date 2022/7/28
 */
@Data
public class DeploymentBean implements Serializable {
    @NotEmpty
    @ApiModelProperty(value = "xml文件内容")
    private String text;

    @NotEmpty
    @ApiModelProperty(value = "部署模板名称")
    private String name;

    @NotEmpty
    @ApiModelProperty(value = "部署来源")
    private String source;


//    @ApiModelProperty(value = "表单id")
//    private String formId;
//
//    @ApiModelProperty(value = "表单文件内容")
//    private Object formText;

    @ApiModelProperty(value = "表单集合")
    private List<FormBean> formBeanList;
}
