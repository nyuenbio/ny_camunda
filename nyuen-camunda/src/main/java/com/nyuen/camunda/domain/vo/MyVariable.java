package com.nyuen.camunda.domain.vo;

import lombok.Data;

/**
 * TODO
 *
 * @author chengjl
 * @description
 * @date 2022/11/10
 */
@Data
public class MyVariable {
    private String variableName;
    private String variableType;
    private Object variableValue;

    public MyVariable(String variableName,String variableType,Object variableValue){
        this.variableName = variableName;
        this.variableType = variableType;
        this.variableValue = variableValue;
    }
}
