package com.coocaa.common.constant;

/**
 * @description: Redis常量
 * @author: dongyang_wu
 * @create: 2019-07-31 09:09
 */
public class RedisConstant {
    public interface Redis {
        String OK = "OK";
        // 过期时间, 60s, 一分钟
        Integer EXPIRE_TIME_MINUTE = 60;
        // 过期时间, 一小时
        Integer EXPIRE_TIME_HOUR = 60 * 60;
        // 过期时间, 一天
        Integer EXPIRE_TIME_DAY = 60 * 60 * 24;
        // 5分钟
        Integer EXPIRE_TIME_FIVE_MINUTE = 5 * 60;
    }

    public interface AUTH_TOKEN {
        String PREFIX = "AUTH_TOKEN";
        String TOKEN_PREFIX = "AUTH_TOKEN_%s";

    }
}