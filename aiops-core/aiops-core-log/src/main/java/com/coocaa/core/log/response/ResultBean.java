package com.coocaa.core.log.response;

import com.coocaa.core.log.exception.ApiResultEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "响应对象")
public class ResultBean<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int SUCCESS = 1;
    private static final String SUCCESS_TIPS = "success";
    private static final String FAIL_TIPS = "error";
    public static final int FAIL = 0;

    public static final int NO_PERMISSION = 2;
    @ApiModelProperty(value = "响应的msg提示")
    private String msg = SUCCESS_TIPS;
    @ApiModelProperty(value = "响应码")
    private int code = SUCCESS;
    @ApiModelProperty(value = "是否成功", required = true)
    private boolean success = true;
    @ApiModelProperty(value = "响应的json数据")
    private T data;

    public ResultBean() {
        super();
    }

    public ResultBean(T data) {
        super();
        this.data = data;
    }

    public ResultBean(T data, int code) {
        super();
        this.data = data;
        this.code = code;
    }


    public ResultBean(T data, String msg) {
        super();
        this.data = data;
        this.msg = msg;
    }

    public ResultBean(String msg, int code) {
        super();
        this.msg = msg;
        this.code = code;
    }

    public ResultBean(Throwable e) {
        super();
        this.msg = e.toString();
        this.code = FAIL;
    }

    public static <T> ResultBean error(T msg) {
        ResultBean error = new ResultBean(FAIL_TIPS, ResultBean.FAIL);
        error.setData(msg);
        error.setSuccess(false);
        return error;
    }

    public static ResultBean error(String msg, Integer code) {
        ResultBean error = new ResultBean(FAIL_TIPS, code);
        error.setData(msg);
        error.setSuccess(false);
        return error;
    }

    public static ResultBean error(ApiResultEnum apiResultEnum) {
        ResultBean error = new ResultBean(FAIL_TIPS, apiResultEnum.getCode());
        error.setData(apiResultEnum.getMessage());
        error.setSuccess(false);
        return error;
    }
    public static ResultBean success(ApiResultEnum apiResultEnum) {
        ResultBean error = new ResultBean(SUCCESS_TIPS, apiResultEnum.getCode());
        error.setData(apiResultEnum.getMessage());
        error.setSuccess(true);
        return error;
    }
    public static ResultBean success(Object msg) {
        return new ResultBean(msg);
    }
}