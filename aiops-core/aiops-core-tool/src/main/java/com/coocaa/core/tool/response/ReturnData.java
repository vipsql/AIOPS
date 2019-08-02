package com.coocaa.core.tool.response;

import lombok.Data;

/**
 * @Auther: wyx
 * @Date: 2019-07-22 15:22
 * @Description: 封装返回值
 */
@Data
public class ReturnData {

    private Integer code;

    private String msg;

    private Object data;

    private ReturnData(Integer code, String msg, Object data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private ReturnData(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public static ReturnData success(){
        return new ReturnData(CodeEnum.REQUEST_SUCCESS.getCode(), CodeEnum.REQUEST_SUCCESS.getMsg());
    }

    public static ReturnData success(Object data){
        return new ReturnData(CodeEnum.REQUEST_SUCCESS.getCode(), CodeEnum.REQUEST_SUCCESS.getMsg(), data);
    }

    public static ReturnData fail(Integer code, String msg){
        return new ReturnData(code, msg);
    }
}
