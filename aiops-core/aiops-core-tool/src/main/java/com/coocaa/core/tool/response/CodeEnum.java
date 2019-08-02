package com.coocaa.core.tool.response;

/**
 * @Auther: wyx
 * @Date: 2019-07-22 15:07
 * @Description: 返回码枚举类
 */
public enum CodeEnum {

    REQUEST_SUCCESS(1001, "请求成功"),
    UN_KNOW_ERROR(2001, "未知异常"),
    PARAM_ILLEGAL(2002, "参数异常"),
    DATABASE_OPERATE_FAILURE(2003, "数据库操作异常"),
    NO_DATA(2004, "所查找数据不存在"),
    SYSTEM_ERROR(2005, "系统错误"),
    TOKEN_INVALID(2006, "登录失效"),
    PROMETHEUS_QUERY_ERROR(2007, "Prometheus 查询错误");

    private Integer code;

    private String msg;

    CodeEnum(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode(){
        return code;
    }

    public String getMsg(){
        return msg;
    }

}
