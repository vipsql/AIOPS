package com.coocaa.prometheus.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coocaa.core.mybatis.base.BaseService;
import com.coocaa.prometheus.entity.Metrics;
import com.coocaa.prometheus.input.MetisCsvInputVo;
import com.coocaa.prometheus.input.MetricsInputVo;
import com.coocaa.prometheus.output.MetricsCsvVo;

import java.util.List;
import java.util.concurrent.ExecutionException;

public interface MetricsService extends BaseService<Metrics> {
    /**
     * 新建指标并启动定时任务
     *
     * @param metrics
     * @return
     */
    Metrics createMetrics(Integer type, MetricsInputVo metricsInputVo);

    /**
     * 删除指定id的指标
     */
    boolean deleteMetrics(Long id, Integer type);

    /**
     * 导出对应指标的Csv训练数据
     */
    List<MetricsCsvVo> exportMetisCsv(MetisCsvInputVo metisCsvInputVo) throws ExecutionException, InterruptedException;

    JSONObject exportMetisCsvToTrain(MetisCsvInputVo metisCsvInputVo) throws ExecutionException, InterruptedException;
}
