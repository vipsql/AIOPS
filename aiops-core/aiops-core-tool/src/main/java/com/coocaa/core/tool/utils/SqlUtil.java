package com.coocaa.core.tool.utils;

import com.coocaa.common.request.RequestBean;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

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

    public static ImmutableMap.Builder<String, Object> map(String query, Object queryString) {
        return ImmutableMap.<String, Object>builder().put(query, queryString);
    }

    public static Map<String, Object> map(RequestBean requestBean) {
        HashMap<String, Object> resultMap = new HashMap<>();
        requestBean.getItems().forEach(item -> {
            resultMap.put(item.getQuery(), item.getQueryString());
        });
        return resultMap;
    }
}