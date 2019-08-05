package com.coocaa.prometheus.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coocaa.prometheus.entity.Metrics;
import com.coocaa.prometheus.input.MetricsInputVo;

public interface MetricsService extends IService<Metrics> {
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
}
