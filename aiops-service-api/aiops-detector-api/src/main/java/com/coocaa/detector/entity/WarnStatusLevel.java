package com.coocaa.detector.entity;


import lombok.Data;

/**
 * @author 陈煜坤
 * @date 2019/7/24  12:56
 * @package_name com.monitoring.data_manipulation.entity
 * 检测水平类
 */
@Data
public class WarnStatusLevel {

    /**
     * 返回码。0:成功；非0:失败
     */
    private int code;
    /**
     * 返回消息
     */
    private String msg;
    /**
     * 检测结果是否异常。0:异常；1:正常
     */
    private int ret;
    /**
     * 概率值，值越小，判定为异常的置信度越高，目前p<0.15，判决为异常
     */
    private String p;

    public WarnStatusLevel(int code, String msg, int ret, String p) {
        this.code = code;
        this.msg = msg;
        this.ret = ret;
        this.p = p;
    }

    public WarnStatusLevel(int code, int ret, String p) {
        this.code = code;
        this.ret = ret;
        this.p = p;
    }

    public WarnStatusLevel(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
