package com.coocaa.core.tool.response;

/**
 * @author 陈煜坤
 * @date 2019/7/24  14:07
 * @package_name com.monitoring.data_manipulation.common
 */
public enum WarnStatusEnum {

    OK(0),
    FAIL(500);

    private int code ;

    WarnStatusEnum(int code) {
        this.code=code;
    }

    public int getCode() {
        return code;
    }
}
