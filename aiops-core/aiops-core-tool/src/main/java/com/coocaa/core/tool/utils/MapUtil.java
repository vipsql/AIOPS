package com.coocaa.core.tool.utils;

import java.util.Map;

/**
 * @program: intelligent-maintenance
 * @description: Map工具类
 * @author: dongyang_wu
 * @create: 2019-08-07 15:29
 */
public class MapUtil {
    /**
     * 获取map中第一个key
     * @param map
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V> K getFirstOrNull(Map<K, V> map) {
        K obj = null;
        for (Map.Entry<K, V> entry : map.entrySet()) {
            obj = entry.getKey();
            if (obj != null) {
                break;
            }
        }
        return obj;
    }
}