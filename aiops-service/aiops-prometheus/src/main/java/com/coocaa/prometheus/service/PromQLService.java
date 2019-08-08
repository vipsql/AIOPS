package com.coocaa.prometheus.service;


import com.coocaa.core.log.response.ResultBean;
import com.coocaa.prometheus.dto.MetisDto;
import com.coocaa.prometheus.entity.*;
import com.coocaa.prometheus.input.QueryMetricProperty;
import com.coocaa.prometheus.output.MetricsCsvVo;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * @Auther: wyx
 * @Date: 2019-07-27 14:29
 * @Description: 与 Prometheus 交互服务
 */
public interface PromQLService {

    ResponseEntity<ResultBean> queryMetrics(Task task, Integer type);

    /**
     * 上传到metis检测异常
     *
     * @param queryMetricProperty
     * @param type
     * @return
     */
    ResponseEntity<ResultBean> exceptionDetect(QueryMetricProperty queryMetricProperty, Integer type) throws ExecutionException, InterruptedException;


    /**
     * 根据指标名获取指标筛选条件
     *
     * @param metricsName
     * @param minute      分钟单位
     * @return
     */
    ResponseEntity<ResultBean> getConditionByMetricsName(String metricsName, Integer minute);

    /**
     * 根据指标名和条件获取相应数据并进行预测,返回最新点是否异常的预测结果值
     *
     * @param date       检测时间点
     * @param metricName 指标名带%s
     * @param span       秒钟单位,距离当前多少秒钟的数据
     * @param step       秒钟单位, 步长
     */
    Map<String, MatrixData> getRangeValues(MetisDto metisDto, Date date, String metricName, Integer span, Integer step, Map<String, String> conditions) throws ExecutionException, InterruptedException;


    /**
     * 获取公司普罗米修斯监控端点
     *
     * @return
     */
    ResponseEntity<ResultBean> getTargets();

    /**
     * 即时查询
     * 如果time缺省，则用当前服务器时间表示执行时刻
     *
     * @param query   query=<string>: Prometheus表达式查询字符串
     * @param date    time=<rfc3339 | unix_timestamp>: 执行时间戳，可选项
     * @param timeout timeout=<duration>: 执行超时时间设置，默认由-query.timeout标志设置
     * @return
     */
    List<VectorData> instantQuery(String query, Date date, Integer timeout);

    /**
     * 范围查询
     *
     * @param query query=<string>: Prometheus表达式查询字符串
     * @param start start=<rfc3339 | unix_timestamp>: 开始时间戳
     * @param end   end=<rfc3339 | unix_timestamp>: 结束时间戳
     * @param step  step=<duration>: 查询时间步长，范围时间内每step秒执行一次
     * @return
     */
    List<MatrixData> rangeQuery(String query, Date start, Date end, Integer step);

    /**
     * 生成Metis训练集CSV文件
     */
    List<MetricsCsvVo> createMetisCsvVo(Date now, String realQuery) throws ExecutionException, InterruptedException;

    /**
     * 范围查询map结果集
     *
     * @param query
     * @param start
     * @param end
     * @param step
     * @return
     */

    List<Map<String, Object>> rangeQueryToList(String query, Date start, Date end, Integer step);


}
