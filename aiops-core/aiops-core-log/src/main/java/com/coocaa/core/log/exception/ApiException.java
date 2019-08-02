package com.coocaa.core.log.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class ApiException extends RuntimeException {
    private Integer code;
    private String message;

    public ApiException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiException(ApiResultEnum apiResultEnum) {
        this.message = apiResultEnum.getMessage();
        this.code = apiResultEnum.getCode();
    }

    public ApiException(String message) {
        this.message = message;
        this.code = 0;
    }
    @Override
    public String toString() {
        return "错误码：" + code + " " + "错误信息：" + message;
    }
}
