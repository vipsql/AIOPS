package com.coocaa.prometheus.util;

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
    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>(16);
    private final Map<Long, AtomicInteger> taskErrorTimes = new ConcurrentHashMap<>(16);
    @Autowired
    private TaskScheduler taskScheduler;
    private volatile Integer notifyNumber = 5;

    public void addCronTask(Long taskId, Runnable task, String cronExpression) {
        if (this.scheduledTasks.containsKey(taskId)) {
            removeCronTask(taskId);
        }
        ScheduledFuture<?> future = taskScheduler.schedule(task, new CronTrigger(cronExpression));
        this.scheduledTasks.put(taskId, future);
    }

    @Transactional
    public synchronized boolean removeCronTask(Long taskId) {
        if (this.scheduledTasks.containsKey(taskId)) {
            ScheduledFuture<?> scheduledTask = this.scheduledTasks.remove(taskId);
            if (scheduledTask != null && !scheduledTask.isCancelled()) {
                scheduledTask.cancel(true);
                log.info("定时任务停止-" + taskId);
                return true;
            }
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
        return taskErrorTimes.get(taskId).getAndIncrement() > notifyNumber;
    }

    public synchronized void deleteErrorTimesMap(Long taskId) {
        taskErrorTimes.remove(taskId);
    }
}