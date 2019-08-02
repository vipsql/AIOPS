package com.monitoring.data_manipulation.service;

import com.monitoring.data_manipulation.entity.MatrixData;
import com.monitoring.data_manipulation.entity.VectorData;

import java.util.Date;
import java.util.List;

/**
 * @Auther: wyx
 * @Date: 2019-07-27 14:29
 * @Description: 与 Prometheus 交互服务
 */
public interface PromQLService {

    /**
     * 即时查询
     * 如果time缺省，则用当前服务器时间表示执行时刻
     * @param query query=<string>: Prometheus表达式查询字符串
     * @param date time=<rfc3339 | unix_timestamp>: 执行时间戳，可选项
     * @param timeout timeout=<duration>: 执行超时时间设置，默认由-query.timeout标志设置
     * @return
     */
    List<VectorData> instantQuery(String query, Date date, Integer timeout);

    /**
     * 范围查询
     * @param query query=<string>: Prometheus表达式查询字符串
     * @param start start=<rfc3339 | unix_timestamp>: 开始时间戳
     * @param end end=<rfc3339 | unix_timestamp>: 结束时间戳
     * @param step step=<duration>: 查询时间步长，范围时间内每step秒执行一次
     * @return
     */
    List<MatrixData> rangeQuery(String query, Date start, Date end, Integer step);

    /**
     * 查询元数据
     * @param match match[]=<series_selector>: 选择器是series_selector。这个参数个数必须大于等于1
     * @param start start=<rfc3339 | unix_timestamp>: 开始时间戳
     * @param end end=<rfc3339 | unix_timestamp>: 结束时间戳
     * @return
     */
    String metadataQuery(List<String> match, Date start, Date end);

    /**
     * 查询标签值
     * @param label 标签
     * @return
     */
    String labelQuery(String label);

}
