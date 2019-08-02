package com.coocaa.core.tool.utils;

import com.google.common.collect.ImmutableMap;

/**
 * @program: intelligent_maintenance
 * @description: 数据库工具类
 * @author: dongyang_wu
 * @create: 2019-07-30 16:48
 */
public class SqlUtil {
    public static ImmutableMap.Builder<String, Object> map(String query, String queryString) {
        return ImmutableMap.<String, Object>builder().put(query, queryString);
    }
}