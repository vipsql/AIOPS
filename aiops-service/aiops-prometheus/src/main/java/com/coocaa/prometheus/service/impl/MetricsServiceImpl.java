package com.coocaa.prometheus.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coocaa.common.constant.Constant;
import com.coocaa.common.constant.TableConstant;
import com.coocaa.common.request.RequestBean;
import com.coocaa.prometheus.entity.Metrics;
import com.coocaa.prometheus.entity.Task;
import com.coocaa.prometheus.input.MetricsInputVo;
import com.coocaa.prometheus.input.TaskInputVo;
import com.coocaa.prometheus.mapper.MetricsMapper;
import com.coocaa.prometheus.service.MetricsService;
import com.coocaa.prometheus.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * @description: 指标service实现类
 * @author: dongyang_wu
 * @create: 2019-08-05 15:33
 */
@Service
@AllArgsConstructor
public class MetricsServiceImpl extends ServiceImpl<MetricsMapper, Metrics> implements MetricsService {
    private TaskService taskService;
    private MetricsMapper metricsMapper;

    @Override
    @Transactional
    public Metrics createMetrics(Integer type, MetricsInputVo metricsInputVo) {
        Metrics metrics = new Metrics();
        BeanUtils.copyProperties(metricsInputVo, metrics);
        metrics.setQueryRangeJson(JSON.toJSONString(metricsInputVo.getQueryRange()));
        if (Constant.NumberType.ONE_PROPERTY.equals(type)) {
            TaskInputVo taskInputVo = TaskInputVo.builder()
                    .type(Constant.NumberType.ZERO_PROPERTY)
                    .queryRange(metricsInputVo.getQueryRange())
                    .taskCron(metricsInputVo.getTaskCron())
                    .taskDescription("定期获取对应指标数据")
                    .taskName(metricsInputVo.getMetricName())
                    .queryMetric(metricsInputVo.getMetricName())
                    .taskId(metricsInputVo.getTaskId())
                    .build();
            Task queryMetricsTask = taskService.createQueryMetricsTask(taskInputVo);
            metrics.setTaskId(queryMetricsTask.getTaskId());
        }
        metrics.insertOrUpdate();
        return metrics;
    }

    @Override
    @Transactional
    public boolean deleteMetrics(Long id, Integer type) {
        Metrics metrics = metricsMapper.selectById(id);
        RequestBean requestBean = RequestBean.builder().items(Arrays.asList(RequestBean.RequestItem.builder().query(TableConstant.TASK.TASK_ID).queryString(metrics.getTaskId() + "").build())).build();
        taskService.removeQueryMetricsTask(requestBean, type);
        return metrics.deleteById();
    }
}