package com.coocaa.data.manipulation.util.runnable;

import com.coocaa.common.constant.Constant;
import com.coocaa.data.manipulation.entity.*;
import com.coocaa.data.manipulation.event.ErrorTaskPublisher;
import com.coocaa.data.manipulation.util.TaskManager;

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
    private ThreadLocal<Integer> errorTimes;

    public QueryMetricTask(Task task, Integer type) {
        this.task = task;
        this.type = type;
        this.errorTimes = new ThreadLocal<>();
    }

    @Override
    public void run() {
        try {
            if (Constant.NumberType.ZERO_PROPERTY.equals(type)) {
                QueryInstant queryInstant = task.getQueryInstant();
                List<VectorData> vectorData = TaskManager.getPromQLService().instantQuery(queryInstant.getQuery(), queryInstant.getDate(), queryInstant.getTimeout());
                System.out.println(vectorData);
            } else if (Constant.NumberType.ONE_PROPERTY.equals(type)) {
                QueryRange queryRange = task.getQueryRange();
                List<MatrixData> matrixData = TaskManager.getPromQLService().rangeQuery(queryRange.getQuery(), queryRange.getStart(), queryRange.getEnd(), queryRange.getStep());
                System.out.println(matrixData);
            }
        } catch (Exception e) {
            this.errorTimes.set(this.errorTimes.get() + 1);
            if (this.errorTimes.get() > 5) {
                System.out.println("定时任务异常次数超过5次,即将退出....");
                ErrorTaskPublisher.publishEvent(task.getTaskId(), e);
            }
        }
    }
}