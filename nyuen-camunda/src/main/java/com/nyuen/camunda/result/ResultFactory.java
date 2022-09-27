package com.nyuen.camunda.result;

public class ResultFactory {

    public static Result buildSuccessResult(Object data) {
        return buildResult(ResultEnums.SUCCESS.getCode(), "成功", data);
    }

    public static Result buildFailResult(String message) {
        return buildResult(ResultCode.FAIL, message, null);
    }

    public static Result buildResult(ResultCode resultCode, String message, Object data) {
        return buildResult(resultCode.code, message, data);
    }

    public static Result buildResult(int code, String message, Object data) {
        return new Result(code, message, data);
    }

    public static Result buildLoginFailResult(String message) {
        return buildResult(ResultCode.UNAUTHORIZED, message, "");
    }

    public static Result buildResult(ResultEnums resultEnums, Object data) {
        return new Result(resultEnums, data);
    }


}
