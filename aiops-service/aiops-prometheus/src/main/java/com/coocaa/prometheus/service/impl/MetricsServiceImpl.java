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
import com.coocaa.prometheus.constant.KPIListing;
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
        metrics.insertOrUpdate();
        if (Constant.NumberType.ONE_PROPERTY.equals(type)) {
            TaskInputVo taskInputVo = TaskInputVo.builder()
                    .id(metricsInputVo.getTaskId())
                    .type(Constant.NumberType.ZERO_PROPERTY)
                    .queryRange(metricsInputVo.getQueryRange())
                    .taskCron(metricsInputVo.getTaskCron())
                    .taskDescription("定期获取对应指标数据")
                    .taskName(metricsInputVo.getTaskName())
                    .metricsId(metrics.getId())
                    .modelName(metricsInputVo.getModelName())
                    .build();
            taskService.createQueryMetricsTask(taskInputVo);
        }
        return metrics;
    }

    @Override
    public Map<String, String> getKPIListing() {
        return KPIListing.KPI;
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
                List<Task> tasks = taskService.getBaseMapper().selectByMap(SqlUtil.map(TableConstant.TASK.METRICS_ID, metric.getId()).build());
                RequestBean taskDeleteRequestBean = new RequestBean();
                List<RequestBean.RequestItem> itemList = new ArrayList<>();
                tasks.forEach(task -> {
                    RequestBean.RequestItem item = RequestBean.RequestItem.builder().query(TableConstant.ID).queryString(task.getId() + "").build();
                    itemList.add(item);
                });
                taskDeleteRequestBean.setItems(itemList);
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
        List<Task> tasks = taskService.getBaseMapper().selectByMap(SqlUtil.map(TableConstant.TASK.METRICS_ID, metisCsvInputVo.getMetricsId()).build());
        List<MetricsCsvVo> allResultList = new ArrayList<>();
        // 遍历指标下的所有定时任务
        for (Task task : tasks) {
            begin = metisCsvInputVo.getBegin();
            QueryRange queryRange = JSON.parseObject(task.getArgs(), QueryRange.class);
            String realQuery = PromQLUtil.getQueryConditionStr(queryRange.getQuery(), queryRange.getConditions());
            while (true) {
                List<MetricsCsvVo> metisCsvVo = promQLService.createMetisCsvVo(begin, realQuery, 1857889L, 20158L);
                allResultList.addAll(metisCsvVo);
                begin = DateUtil.setMinutes(begin, span);
                if (begin.compareTo(end) > 0)
                    break;
            }
        }
        return allResultList;
    }

    @Override
    public JSONObject exportMetisCsvToTrain(MetisCsvInputVo metisCsvInputVo) throws ExecutionException, InterruptedException {
        List<MetricsCsvVo> metricsCsvVos = exportMetisCsv(metisCsvInputVo);
        //RestTemplate带参传的时候要用HttpEntity<?>对象传递
        Map<String, Object> map = new HashMap<>();
        map.put("metrics", metricsCsvVos);
        String s = JSON.toJSONString(map);
        System.out.println(s);
        String apiResult = restTemplate.postForObject("http://123.56.7.250:8080/ImportSample", s, String.class);
        JSONObject data = JSON.parseObject(apiResult).getJSONObject("data");
        return data;
    }
}