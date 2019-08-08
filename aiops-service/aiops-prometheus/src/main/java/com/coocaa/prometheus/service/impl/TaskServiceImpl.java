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
import com.coocaa.prometheus.entity.QueryRange;
import com.coocaa.prometheus.entity.Task;
import com.coocaa.prometheus.input.MetisCsvInputVo;
import com.coocaa.prometheus.input.TaskInputVo;
import com.coocaa.prometheus.mapper.TaskMapper;
import com.coocaa.prometheus.output.MetricsCsvVo;
import com.coocaa.prometheus.service.PromQLService;
import com.coocaa.prometheus.service.TaskService;
import com.coocaa.prometheus.util.PromQLUtil;
import com.coocaa.prometheus.util.TaskManager;
import com.coocaa.prometheus.util.runnable.QueryMetricTask;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;


/**
 * @description: 定时任务调度类
 * @author: dongyang_wu
 * @create: 2019-08-01 13:46
 */
@Service
@AllArgsConstructor
public class TaskServiceImpl extends BaseServiceImpl<TaskMapper, Task> implements TaskService {
    private TaskManager taskManager;
    private TaskMapper taskMapper;
    private RestTemplate restTemplate;
    private PromQLService promQLService;

    @Override
    public Task createQueryMetricsTask(TaskInputVo taskInputVo) {
        Task task = new Task();
        BeanUtils.copyProperties(taskInputVo, task);
        QueryRange queryRange = task.getQueryRange();
        if (queryRange != null)
            task.setArgs(JSON.toJSONString(queryRange));
        task.setStatus(Constant.NumberType.GOOD_PROPERTY);
        boolean insert = task.insertOrUpdate();
        if (insert) {
            QueryMetricTask queryMetricTask = new QueryMetricTask(task);
            taskManager.addCronTask(task.getId(), queryMetricTask, task.getTaskCron());
        }
        return task;
    }

    @Override
    @Transactional
    public Boolean removeQueryMetricsTask(RequestBean requestbean, Integer type) {
        Set<Long> taskIdSets = new HashSet<>();
        requestbean.getItems().forEach(item -> {
            List<Task> tasks = taskMapper.selectByMap(SqlUtil.map(item.getQuery(), item.getQueryString()).build());
            if (Constant.NumberType.TWO_PROPERTY.equals(type)) {
                // 禁用指定的定时任务
                tasks.forEach(task -> {
                    task.setStatus(Constant.NumberType.BAD_PROPERTY);
                    task.insertOrUpdate();
                });
            }
            // 获取指定的sets
            List<Long> ids = tasks.stream().map(Task::getId).collect(Collectors.toList());
            taskIdSets.addAll(ids);
        });
        boolean deleteFlag = Constant.NumberType.ZERO_PROPERTY.equals(type);
        taskIdSets.forEach(item -> {
            taskManager.removeCronTask(item);
            if (deleteFlag) {
                taskMapper.deleteByMap(SqlUtil.map(TableConstant.ID, item.toString()).build());
            }
        });
        return true;
    }

    @Override
    public void bootstrapAllTask() {
        List<Task> tasks = taskMapper.findAll();
        tasks.forEach(task -> {
            QueryMetricTask queryMetricTask = new QueryMetricTask(task);
            taskManager.addCronTask(task.getId(), queryMetricTask, task.getTaskCron());
        });
    }

    @Override
    public List<MetricsCsvVo> exportMetisCsv(MetisCsvInputVo metisCsvInputVo) throws ExecutionException, InterruptedException {
        Date begin = metisCsvInputVo.getBegin();
        Date end = metisCsvInputVo.getEnd();
        Integer span = metisCsvInputVo.getSpan();
        if (begin.compareTo(end) > 0 || span < 0)
            throw new ApiException(ApiResultEnum.FUNCTION_PARAMETER_SCOPE_ERROR);
        Task task = taskMapper.selectById(metisCsvInputVo.getTaskId());
        QueryRange queryRange = JSON.parseObject(task.getArgs(), QueryRange.class);
        String realQuery = PromQLUtil.getQueryConditionStr(queryRange.getQuery(), queryRange.getConditions());
        List<MetricsCsvVo> resultList = new ArrayList<>();
        while (true) {
            List<MetricsCsvVo> metisCsvVo = promQLService.createMetisCsvVo(begin, realQuery);
            resultList.addAll(metisCsvVo);
            begin = DateUtil.setMinutes(begin, span);
            if (begin.compareTo(end) > 0)
                break;
        }
        return resultList;
    }

    @Override
    public JSONObject exportMetisCsvToTrain(MetisCsvInputVo metisCsvInputVo) throws ExecutionException, InterruptedException {
        List<MetricsCsvVo> metricsCsvVos = exportMetisCsv(metisCsvInputVo);
        //RestTemplate带参传的时候要用HttpEntity<?>对象传递
        Map<String, Object> map = new HashMap<>();
        map.put("metrics", metricsCsvVos);
        String s = JSON.toJSONString(map);
        System.out.println(s);
        String apiResult = restTemplate.postForObject("http://172.20.146.81:8087/ImportSample", s, String.class);
        JSONObject data = JSON.parseObject(apiResult).getJSONObject("data");
        return data;
    }

    @Override
    public void restartTask(RequestBean requestBean) {
        requestBean.getItems().forEach(item -> {
            List<Task> tasks = taskMapper.selectByMap(SqlUtil.map(item.getQuery(), item.getQueryString()).build());
            tasks.forEach(task -> {
                QueryMetricTask queryMetricTask = new QueryMetricTask(task);
                taskManager.addCronTask(task.getId(), queryMetricTask, task.getTaskCron());
            });
        });
    }

    /**
     * 批量删除定时任务
     */
    @Override
    public void deletes(RequestBean requestBean) {
        requestBean.getItems().forEach(item -> {
            List<Task> tasks = taskMapper.selectByMap(SqlUtil.map(item.getQuery(), item.getQueryString()).build());
            tasks.forEach(task -> {
                taskManager.removeCronTask(task.getId());
                task.deleteById();
            });
        });
    }
}