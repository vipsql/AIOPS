package com.coocaa.core.log.exception;

import lombok.Getter;

@Getter
public enum ApiResultEnum {
    // 成功
    SUCCESS(0, "操作成功"),
    ERROR(-1, "操作失败"),
    MODE_ERROR_CREATE_PPT_OR_PDF(100000, "生成PPT或PDF失败，mode没有正确选择"),
    FILE_ERROR(10000, "文件导入或导出失败"),
    FILE_EMPTY_ERROR(10001, "文件为空,请重新导入"),
    FILE_EXIST_ERROR(10002, "文件已经存在，请重新上传"),

    USER_LOGIN_ERROR(20000, "用户登录认证失败"),
    TOKEN_INVALID(20001, "token无效或过期"),
    USER_NOT_EXIST(20002, "用户不存在"),
    USERNAME_OR_PASSWORD_ERROR(20003, "用户名或者密码错误"),
    USER_LOCKED(20004, "账号锁定"),
    USER_MAIL_ERROR(20005, "邮箱不合法"),
    USER_PHONE_ERROR(20006, "手机号码不合法"),
    USER_USERNAME_EMAIL_ERROR(20007, "用户名邮件一致"),
    USER_USERNAME_PHONE_ERROR(20008, "用户名手机号号码一致"),
    USER_EXIST(20009, "用户名、邮箱或手机号已经存在"),
    USER_NOT_LOGIN(20010, "用户未登录"),
    USER_COPY_USERVO_ERROR(20011, "user对象copy为uservo长度为0"),
    WX_USER_OPENID_GET_ERROR(20012, "获取openid或session_key为空"),
    WX_USER_OPENID_NOT_EXIST(20013, "此openid对应的用户不存在"),
    ORDER_NOT_EXIST(30000, "用户未授权"),
    ACTIVATE_EXPIRE(30001, "激活邮件过期"),
    VERTIFY_EXPIRE(30002, "登录验证码过期"),
    USER_SAVE_ERROR(30003, "用户存储失败"),
    FUNCTION_PARAMETER_SCOPE_ERROR(40000, "接口参数范围错误"),
    MESSAGE_SEND_ERROR(90000, "短信下发失败"),
    VERIFY_PARAM_ERROR(90001, "短信校验码错误"),
    VERIFY_PARAM_PASS(90002, "短信校验码过期"),
    SERVER_BUSY(100000, "服务器繁忙，请稍后再试!"),
    ENTITY_NOT_EXIST(110000, "实体类不存在"),
    QUERY_ARGS_ERROR(110001, "参数组合有误"),
    FUNCTION_NOT_EXEC_ERROR(110002, "函数流程未正确执行"),
    FUNCTION_EXCEPTION(110003, "函数执行出现异常"),
    ;
    private Integer code;
    private String message;

    ApiResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
