package com.coocaa.prometheus.util.runnable;

import com.alibaba.fastjson.JSON;
import com.coocaa.common.constant.Constant;
import com.coocaa.core.tool.utils.SpringUtil;
import com.coocaa.detector.entity.DetectorResult;
import com.coocaa.prometheus.dto.MetisDto;
import com.coocaa.prometheus.entity.*;
import com.coocaa.prometheus.event.ErrorTaskPublisher;
import com.coocaa.prometheus.util.TaskManager;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @description: 查询指标线程
 * @author: dongyang_wu
 * @create: 2019-08-01 14:12
 */
public class QueryMetricTask implements Runnable {
    private Task task;
    private Integer type;

    public QueryMetricTask(Task task, Integer type) {
        this.task = task;
        this.type = type;
    }

    @Override
    public void run() {
        try {
            if (Constant.NumberType.ZERO_PROPERTY.equals(type)) {
                QueryRange queryRange = task.getQueryRange();
                if (queryRange == null) {
                    queryRange = JSON.parseObject(task.getArgs(), QueryRange.class);
                }
                Metrics metrics = TaskManager.getMetricsService().getBaseMapper().selectById(task.getMetricsId());
                MetisDto metisDto = MetisDto.builder()
                        .viewId(metrics.getId())
                        .viewName(metrics.getMetricName())
                        .attrId(task.getId())
                        .attrName(task.getTaskName())
                        .modelName(task.getModelName()).build();
                Map<String, MatrixData> rangeValues = TaskManager.getPromQLService().getRangeValues(metisDto, new Date(), queryRange.getQuery(), queryRange.getSpan(), queryRange.getStep(), queryRange.getConditions());
                Map<String, MatrixData> errorItems = new ConcurrentHashMap<>(16);
                rangeValues.forEach((key, value) -> {
                    DetectorResult.DataBean detectResult = value.getDetectResult();
                    // 没有匹配的dataA、dataB、dataC
                    if (detectResult == null)
                        return;
                    if (detectResult.getRet() == Constant.MetisException.RET || Double.valueOf(detectResult.getP()) < Constant.MetisException.P) {
                        errorItems.put(key, value);
                    }
                });
                MetisException metisException = MetisException.builder()
                        .createTime(new Date())
                        .matrixDataJson(JSON.toJSONString(errorItems))
                        .status(Constant.NumberType.ZERO_PROPERTY)
                        .metricsId(task.getMetricsId())
                        .build();
                metisException.insertOrUpdate();
                TaskManager.getAsyncServiceTask().sendDetectResult(errorItems, task.getMetricsId());
                // TaskManager.getTimingDataSender().send(JSONArray.toJSONString(rangeValues));
            }
        } catch (Exception e) {
            System.out.println(e);
            TaskManager taskManager = SpringUtil.getBean("TaskManager");
            if (taskManager.isOverErrorTimes(task.getId())) {
                taskManager.removeCronTask(task.getId());
                taskManager.deleteErrorTimesMap(task.getId());
                ErrorTaskPublisher.publishEvent(task.getTaskDescription(), e);
                System.out.println("定时任务异常次数超过5次,即将退出....");
            }
        }
    }
}