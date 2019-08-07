package com.coocaa.prometheus.util;

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @program: intelligent_maintenance
 * @description: 普罗米修斯语法工具类
 * @author: dongyang_wu
 * @create: 2019-08-02 13:57
 */
public class PromQLUtil {

    public static String getQueryConditionStr(String key, String value) {
        StringBuffer sb = new StringBuffer();
        sb.append(key).append("=\"").append(value).append("\"");
        return sb.toString();
    }

    public static String getQueryConditionStr(String metricsName, Map<String, String> conditions) {
        if (conditions == null)
            return metricsName.replace("%s", "");
        List<String> conditionQuery = new ArrayList<>();
        conditions.forEach((key, value) -> conditionQuery.add(getQueryConditionStr(key, value)));
        if (!CollectionUtils.isEmpty(conditionQuery)) {
            StringBuffer condition = new StringBuffer();
            condition.append("{").append(StringUtils.collectionToDelimitedString(conditionQuery, ",")).append("}");
            return metricsName.replaceAll("%s", condition.toString());
        }
        return metricsName.replace("%s", "");
    }
}