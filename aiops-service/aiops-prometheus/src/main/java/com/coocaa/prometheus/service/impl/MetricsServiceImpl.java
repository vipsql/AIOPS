package com.coocaa.prometheus.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.coocaa.common.constant.Constant;
import com.coocaa.common.constant.TableConstant;
import com.coocaa.common.request.RequestBean;
import com.coocaa.core.log.exception.ApiException;
import com.coocaa.core.log.exception.ApiResultEnum;
import com.coocaa.core.mybatis.base.BaseServiceImpl;
import com.coocaa.core.tool.utils.DateUtil;
import com.coocaa.core.tool.utils.SqlUtil;
import com.coocaa.prometheus.entity.*;
import com.coocaa.prometheus.input.*;
import com.coocaa.prometheus.mapper.MetricsMapper;
import com.coocaa.prometheus.output.MetricsCsvVo;
import com.coocaa.prometheus.service.*;
import com.coocaa.prometheus.util.PromQLUtil;
import lombok.AllArgsConstructor;
import org.json.*;
import org.springframework.beans.BeanUtils;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * @description: 指标service实现类
 * @author: dongyang_wu
 * @create: 2019-08-05 15:33
 */
@Service
@AllArgsConstructor
public class MetricsServiceImpl extends BaseServiceImpl<MetricsMapper, Metrics> implements MetricsService {
    private TaskService taskService;
    private MetricsMapper metricsMapper;
    private PromQLService promQLService;
    private RestTemplate restTemplate;

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

    /**
     * 批量删除
     *
     * @param requestBean
     */
    @Override
    public void deletes(RequestBean requestBean) {
        // 删除定时任务
        Integer type = Constant.NumberType.ZERO_PROPERTY;
        requestBean.getItems().forEach(request -> {
            List<Metrics> metrics = metricsMapper.selectByMap(SqlUtil.map(request.getQuery(), request.getQueryString()).build());
            metrics.forEach(metric -> {
                RequestBean taskDeleteRequestBean = RequestBean.builder().items(Arrays.asList(RequestBean.RequestItem.builder().query(TableConstant.TASK.TASK_ID).queryString(metric.getTaskId() + "").build())).build();
                taskService.removeQueryMetricsTask(taskDeleteRequestBean, type);
                metric.deleteById();
            });
        });
    }

    @Override
    public List<MetricsCsvVo> exportMetisCsv(MetisCsvInputVo metisCsvInputVo) throws ExecutionException, InterruptedException {
        Date begin = metisCsvInputVo.getBegin();
        Date end = metisCsvInputVo.getEnd();
        Integer span = metisCsvInputVo.getSpan();
        if (begin.compareTo(end) > 0 || span < 0)
            throw new ApiException(ApiResultEnum.FUNCTION_PARAMETER_SCOPE_ERROR);
        Metrics metrics = metricsMapper.selectById(metisCsvInputVo.getMetricsId());
        QueryRange queryRange = JSON.parseObject(metrics.getQueryRangeJson(), QueryRange.class);
        String realQuery = PromQLUtil.getQueryConditionStr(queryRange.getQuery(), queryRange.getConditions());
        List<MetricsCsvVo> allResultList = new ArrayList<>();
        while (true) {
            List<MetricsCsvVo> metisCsvVo = promQLService.createMetisCsvVo(begin, realQuery, 1857889L, 20158L);
            allResultList.addAll(metisCsvVo);
            begin = DateUtil.setMinutes(begin, span);
            if (begin.compareTo(end) > 0)
                break;
        }
        return allResultList;
    }

    @Override
    public JSONObject exportMetisCsvToTrain(MetisCsvInputVo metisCsvInputVo) throws ExecutionException, InterruptedException {
        List<MetricsCsvVo> metricsCsvVos = exportMetisCsv(metisCsvInputVo);
        //RestTemplate带参传的时候要用HttpEntity<?>对象传递
        Map<String, Object> map = new HashMap<>();
        map.put("metrics", metricsCsvVos);
        String apiResult = restTemplate.postForObject("http://123.56.7.250:8080/ImportSample", JSON.toJSONString(map), String.class);
        JSONObject data = JSON.parseObject(apiResult).getJSONObject("data");
        return data;
    }
}