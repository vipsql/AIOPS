package com.coocaa.prometheus.util.runnable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.coocaa.common.constant.Constant;
import com.coocaa.core.tool.utils.SpringUtil;
import com.coocaa.prometheus.entity.*;
import com.coocaa.prometheus.event.ErrorTaskPublisher;
import com.coocaa.prometheus.util.TaskManager;

import java.util.List;

/**
 * @program: intelligent_maintenance
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
                QueryInstant queryInstant = task.getQueryInstant();
                if (queryInstant == null) {
                    queryInstant = JSON.parseObject(task.getArgs(), QueryInstant.class);
                }
                List<VectorData> vectorData = TaskManager.getPromQLService().instantQuery(queryInstant.getQuery(), queryInstant.getDate(), queryInstant.getTimeout());
                System.out.println(vectorData);
                vectorData.forEach(item -> item.setTaskId(task.getTaskId()));
                TaskManager.getTimingDataSender().send(JSONArray.toJSONString(vectorData));
            } else if (Constant.NumberType.ONE_PROPERTY.equals(type)) {
                QueryRange queryRange = task.getQueryRange();
                if (queryRange == null) {
                    queryRange = JSON.parseObject(task.getArgs(), QueryRange.class);
                }
                List<MatrixData> matrixData = TaskManager.getPromQLService().rangeQuery(queryRange.getQuery(), queryRange.getStart(), queryRange.getEnd(), queryRange.getStep());
                System.out.println(matrixData);
                matrixData.forEach(item -> item.setTaskId(task.getTaskId()));
                TaskManager.getTimingDataSender().send(JSONArray.toJSONString(matrixData));
            }
        } catch (Exception e) {
            TaskManager taskManager = SpringUtil.getBean("TaskManager");
            if (taskManager.isOverErrorTimes(task.getTaskId())) {
                taskManager.removeCronTask(task.getTaskId());
                taskManager.deleteErrorTimesMap(task.getTaskId());
                ErrorTaskPublisher.publishEvent(task.getTaskDescription(), e);
                System.out.println("定时任务异常次数超过5次,即将退出....");
            }
        }
    }
}