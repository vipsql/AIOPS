package com.coocaa.prometheus.service.impl;

import com.alibaba.fastjson.JSON;
import com.coocaa.common.constant.Constant;
import com.coocaa.common.constant.TableConstant;
import com.coocaa.common.request.RequestBean;
import com.coocaa.core.log.exception.ApiException;
import com.coocaa.core.log.exception.ApiResultEnum;
import com.coocaa.core.mybatis.base.BaseServiceImpl;
import com.coocaa.core.tool.api.R;
import com.coocaa.core.tool.utils.DateUtil;
import com.coocaa.core.tool.utils.SqlUtil;
import com.coocaa.detector.entity.Train;
import com.coocaa.detector.feign.IDetectorClient;
import com.coocaa.prometheus.dto.MetisDto;
import com.coocaa.prometheus.entity.*;
import com.coocaa.prometheus.input.MetisCsvInputVo;
import com.coocaa.prometheus.input.TaskInputVo;
import com.coocaa.prometheus.mapper.KpiMapper;
import com.coocaa.prometheus.mapper.TaskMapper;
import com.coocaa.prometheus.output.MetisCsvOutputVo;
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
    private PromQLService promQLService;
    private IDetectorClient detectorClient;
    private KpiMapper kpiMapper;

    @Override
    public Task createQueryMetricsTask(TaskInputVo taskInputVo, Integer type) {
        Task task = new Task();
        BeanUtils.copyProperties(taskInputVo, task);
        QueryRange queryRange = task.getQueryRange();
        if (queryRange != null)
            task.setArgs(JSON.toJSONString(queryRange));
        task.setStatus(Constant.NumberType.GOOD_PROPERTY);
        boolean insert = task.insertOrUpdate();
        if (insert && Constant.NumberType.ONE_PROPERTY.equals(type)) {
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
        String source = metisCsvInputVo.getSource();
        String trainOrTest = metisCsvInputVo.getTrainOrTest();
        if (begin.compareTo(end) > 0 || span < 0)
            throw new ApiException(ApiResultEnum.FUNCTION_PARAMETER_SCOPE_ERROR);
        // 指标
        Task task = taskMapper.selectById(metisCsvInputVo.getTaskId());
        if (task == null) {
            throw new ApiException(ApiResultEnum.ENTITY_NOT_EXIST);
        }
        // 指标集
        Kpi kpi = kpiMapper.selectById(task.getMetricsId());
        if (kpi == null) {
            throw new ApiException(ApiResultEnum.ENTITY_NOT_EXIST);
        }
        QueryRange queryRange = JSON.parseObject(task.getArgs(), QueryRange.class);
        String realQuery = PromQLUtil.getQueryConditionStr(queryRange.getQuery(), queryRange.getConditions());
        List<MetricsCsvVo> resultList = new ArrayList<>();
        do {
            MetisDto metisDto = MetisDto.builder()
                    .viewId(kpi.getId()).viewName(kpi.getName())
                    .attrId(task.getId()).attrName(task.getTaskName())
                    .source(source).trainOrTest(trainOrTest)
                    .build();
            List<MetricsCsvVo> metisCsvVo = promQLService.createMetisCsvVo(begin, realQuery, metisDto);
            resultList.addAll(metisCsvVo);
            begin = DateUtil.setMinutes(begin, span);
        } while (begin.compareTo(end) <= 0);
        return resultList;
    }

    @Override
    public MetisCsvOutputVo exportMetisCsvToMark(MetisCsvInputVo metisCsvInputVo) throws ExecutionException, InterruptedException {
        //        String apiResult = restTemplate.postForObject("http://172.20.146.81:8087/ImportSample", s, String.class);
//        JSONObject data = JSON.parseObject(apiResult).getJSONObject("data");
        List<MetricsCsvVo> metricsCsvVos = exportMetisCsv(metisCsvInputVo);
        return MetisCsvOutputVo.builder().metisCsvInputVo(metisCsvInputVo).metricsCsvVos(metricsCsvVos).build();
    }

    @Override
    public void exportMetisCsvToTrain(MetisCsvOutputVo trainVos, String modelName) {
        Map<String, Object> map = new HashMap<>();
        map.put("metrics", trainVos.getMetricsCsvVos());
        R<String> importRpcResult = detectorClient.importSample(map);
        MetisCsvInputVo metisCsvInputVo = trainVos.getMetisCsvInputVo();
        // 导入样本成功 开始训练
        if (importRpcResult.isSuccess()) {
            Train train = Train.builder()
                    .positiveOrNegative("")
                    .beginTime(metisCsvInputVo.getBegin().getTime() / 1000).endTime(metisCsvInputVo.getEnd().getTime() / 1000)
                    .source(Arrays.asList(metisCsvInputVo.getSource())).trainOrTest(Arrays.asList(metisCsvInputVo.getTrainOrTest()))
                    .model_name(modelName)
                    .build();
            R<Boolean> trainRpcResult = detectorClient.train(train);
        }
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