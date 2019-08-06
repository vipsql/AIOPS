package com.coocaa.core.tool.utils;

import com.coocaa.common.constant.StringConstant;
import com.coocaa.common.request.PageRequestBean;
import com.coocaa.common.request.RequestBean;
import com.google.common.collect.ImmutableMap;

import java.util.*;
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

    public static String getConditionString(List<PageRequestBean.PageRequestItem> conditions, String conditionConnection) {
        if (CollectionUtil.isNotEmpty(conditions)) {
            StringBuffer sql = new StringBuffer();
            conditions.forEach(condition -> {
                if (StringConstant.LIKE.equalsIgnoreCase(condition.getConnection())) {
                    condition.setQueryString(StringUtil.addPercentageSign(condition.getQueryString()));
                } else if (StringConstant.GREATER_STR.equalsIgnoreCase(condition.getConnection())) {
                    condition.setConnection(StringConstant.GREATER);
                } else if (StringConstant.LESS_STR.equalsIgnoreCase(condition.getConnection())) {
                    condition.setConnection(StringConstant.LESS);
                }
                sql.append(condition.getQuery())
                        .append(" ").append(condition.getConnection()).append(" ")
                        .append("\'").append(condition.getQueryString()).append("\'")
                        .append(" ").append(conditionConnection).append(" ");
            });
            if (StringConstant.AND.equalsIgnoreCase(conditionConnection))
                return sql.toString().substring(0, sql.length() - 4);
            return sql.toString().substring(0, sql.length() - 3);
        }
        return null;
    }
}