package com.coocaa.common.request;

/**
 * @program: intelligent_maintenance
 * @description: 请求工具类
 * @author: dongyang_wu
 * @create: 2019-08-01 12:38
 */
public class RequestUtil {

    public static boolean isInValidParameter(Integer min, Integer max, Integer... list) {
        for (Integer args : list) {
            if (args < min || args > max)
                return true;
        }
        return false;
    }
}