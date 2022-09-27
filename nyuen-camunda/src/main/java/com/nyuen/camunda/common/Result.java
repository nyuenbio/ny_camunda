package com.nyuen.camunda.common;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * JsonDeserialize
 *
 * @description 统一 API响应结果封装
 */
@ApiModel(value = "统一API响应结果封装")
@Data
public class Result implements Serializable {
    /**
     * 响应状态码
     */
    @ApiModelProperty(value = "响应状态码")
    private Integer code;

    /**
     * 响应提示信息
     */
    @ApiModelProperty(value = "响应提示信息")
    private String message;

    /**
     * 响应结果对象
     */
    @ApiModelProperty(value = "响应结果对象")
    private Object data;

    Result() {
    }

    Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    Result(ResultEnums resultEnums, Object data) {
        this.code = resultEnums.getCode();
        this.message = resultEnums.getMessage();
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String toJson() {
        String dataFormat = "{\"code\":${0},\"message\":${1},\"data\":${2}}";
        dataFormat = dataFormat.replace("${0}", null == this.code ? "" : this.code.toString());
        dataFormat = dataFormat.replace("${1}", null == this.message ? "" : JSON.toJSONString(this.message));
        dataFormat = dataFormat.replace("${2}", null == this.data ? "" : JSON.toJSONString(this.data));
        return dataFormat;
    }

    public static void main(String[] args) {
        Result r = new Result(1, "1", "1");
        System.out.println(JSON.parse(r.toJson()));
    }
}

