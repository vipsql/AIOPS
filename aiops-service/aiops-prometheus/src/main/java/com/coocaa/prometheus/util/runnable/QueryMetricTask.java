package com.coocaa.prometheus.util.runnable;

import com.alibaba.fastjson.JSON;
import com.coocaa.common.constant.Constant;
import com.coocaa.core.tool.utils.DateUtil;
import com.coocaa.core.tool.utils.SpringUtil;
import com.coocaa.prometheus.entity.*;
import com.coocaa.prometheus.event.ErrorTaskPublisher;
import com.coocaa.prometheus.util.TaskManager;

import java.util.Date;
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
                QueryRange queryRange = task.getQueryRange();
                if (queryRange == null) {
                    queryRange = JSON.parseObject(task.getArgs(), QueryRange.class);
                }
                List<MatrixData> rangeValues = TaskManager.getPromQLService().getRangeValues(new Date(), queryRange.getQuery(), queryRange.getSpan(), queryRange.getStep(), queryRange.getConditions());
                // 判断数据是否异常，异常则加入异常列表并发通知给负责人
                // 发给team中的所有成员
                // TaskManager.getTimingDataSender().send(JSONArray.toJSONString(rangeValues));
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