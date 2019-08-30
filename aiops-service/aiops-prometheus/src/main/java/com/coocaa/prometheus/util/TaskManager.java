package com.coocaa.prometheus.util;

import com.coocaa.common.constant.Constant;
import com.coocaa.common.constant.TableConstant;
import com.coocaa.core.tool.singleton.SingleTonContextEnum;
import com.coocaa.prometheus.entity.Task;
import com.coocaa.prometheus.util.runnable.QueryMetricTask;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description: 定时任务管理
 * @author: dongyang_wu
 * @create: 2019-08-01 13:55
 */
@Component("TaskManager")
@Slf4j
public class TaskManager implements DisposableBean {
    @Getter
    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>(16);
    private final Map<Long, AtomicInteger> taskErrorTimes = new ConcurrentHashMap<>(16);
    @Autowired
    private TaskScheduler taskScheduler;

    public void addCronTask(Task task) {
        judgeTaskStopFlag(task, false);
        try {
            Long taskId = task.getId();
            String cronExpression = task.getTaskCron();
            if (this.scheduledTasks.containsKey(taskId)) {
                removeCronTask(taskId);
            }
            ScheduledFuture<?> future = taskScheduler.schedule(new QueryMetricTask(task), new CronTrigger(cronExpression));
            this.scheduledTasks.put(taskId, future);
            this.taskErrorTimes.put(taskId, task.getErrorNumber() == null ? new AtomicInteger(0) : new AtomicInteger(task.getErrorNumber()));
            task.setIsUp(Constant.NumberType.GOOD_PROPERTY);
            task.setInstance(SingleTonContextEnum.INSTANCE.getIpUtil().getCurrentInstance());
            SingletonEnum.INSTANCE.getTaskService().updateById(task);
            log.info("定时任务Id:" + taskId + " 启动,当前线程数:" + Thread.activeCount());
        } catch (Exception e) {
            log.error(e.toString());
        }
    }

    @Transactional
    public synchronized boolean removeCronTask(Long taskId) {
        if (this.scheduledTasks.containsKey(taskId)) {
            ScheduledFuture<?> scheduledTask = this.scheduledTasks.remove(taskId);
            this.taskErrorTimes.remove(taskId);
            if (scheduledTask != null && !scheduledTask.isCancelled()) {
                scheduledTask.cancel(true);
                SingletonEnum.INSTANCE.getTaskService().updateById(Task.builder().id(taskId).isUp(Constant.NumberType.BAD_PROPERTY).instance("").build());
                log.info("定时任务id:" + taskId + " 停止,当前线程数:" + Thread.activeCount());
                return true;
            }
        } else {
            SingletonEnum.INSTANCE.getTaskService().updateById(Task.builder().id(taskId).isUp(Constant.NumberType.BAD_PROPERTY).instance("").build());
            log.info("定时任务id:" + taskId + " 停止,当前线程数:" + Thread.activeCount());
            return true;
        }
        return false;
    }

    @Override
    public void destroy() {
        for (ScheduledFuture<?> scheduledTask : this.scheduledTasks.values()) {
            scheduledTask.cancel(true);
        }
        this.scheduledTasks.clear();
    }

    public synchronized Boolean isOverErrorTimes(Long taskId) {
        taskErrorTimes.putIfAbsent(taskId, new AtomicInteger(0));
        return taskErrorTimes.get(taskId).getAndIncrement() >= TableConstant.TASK.START_ERROR_FLAG_NUMBER;
    }

    public synchronized void deleteErrorTimesMap(Long taskId) {
        taskErrorTimes.remove(taskId);
    }

    public boolean judgeTaskStopFlag(Task task, boolean isUpFlag) {
        boolean isStopFlag = false;
        if (task == null)
            return true;
        if (isUpFlag && !Constant.NumberType.GOOD_PROPERTY.equals(task.getIsUp())) {
            isStopFlag = true;
        } else if (!Constant.NumberType.ZERO_PROPERTY.equals(task.getLogic())) {
            isStopFlag = true;
        } else if (!Constant.NumberType.GOOD_PROPERTY.equals(task.getStatus())) {
            isStopFlag = true;
        }
        return isStopFlag;
    }

}