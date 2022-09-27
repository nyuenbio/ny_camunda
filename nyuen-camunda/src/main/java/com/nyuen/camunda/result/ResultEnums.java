package com.nyuen.camunda.result;

public enum ResultEnums {

    SUCCESS(200, "操作成功"),
    NOT_LOGIN(401, "未登录：未获取到有效的Token"),
    FORBIDDEN(403, "禁止访问"),
    FAIL(400, "操作失败"),
    SYSTEM_ERROR(500, "系统异常"),
    BAD_REQUEST(400, "错误的请求参数"),
    ILLEGAL_ARGUMENT(412, "参数不合法"),
    NOT_FOUND(404, "找不到请求路径！"),
    CONNECTION_ERROR(408, "网络连接请求失败！"),
    BAD_REQUEST_TYPE(405, "错误的请求类型"),
    METHOD_NOT_ALLOWED(416, "不合法的请求方式"),
    DATABASE_ERROR(308, "数据库异常"),
    REPEAT_REGISTER(600, "重复注册"),
    NO_USER_EXIST(601, "用户不存在"),
    INVALID_PASSWORD(602, "密码错误"),
    NO_PERMISSION(603, "没有相应权限！"),
    NOT_MATCH(604, "用户名和密码不匹配"),
    INVALID_MOBILE(606, "无效的手机号码"),
    INVALID_EMAIL(607, "无效的邮箱"),
    INVALID_GENDER(608, "无效的性别"),
    REPEAT_MOBILE(609, "已存在此手机号"),
    REPEAT_EMAIL(610, "已存在此邮箱地址"),
    NO_RECORD(611, "没有查到相关记录"),
    LOGIN_SUCCESS(612, "登陆成功"),
    LOGOUT_SUCCESS(613, "已退出登录"),
    SEND_EMAIL_SUCCESS(614, "邮件已发送，请注意查收"),
    EDIT_PWD_SUCCESS(615, "修改密码成功"),
    No_FileSELECT(616, "未选择文件"),
    FILE_UPLOAD_SUCCESS(617, "上传成功"),
    ERROR_VERIFY_CODE(619, "验证码不正确"),
    ERROR_TOKEN(620, "Token异常");

    private int code;
    private String message;

    private ResultEnums(int code, String message) {
        this.code = code;
        this.message = message;
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

}
