package com.coocaa.core.tool.base;

import lombok.Getter;

/**
 * @Auther: wyx
 * @Date: 2019-07-25 15:29
 * @Description: 基础异常
 */
@Getter
public class BaseException extends RuntimeException {

    private Integer code;

    private String msg;

    public BaseException(Integer code, String msg){
        this.code = code;
        this.msg = msg;
    }

}
