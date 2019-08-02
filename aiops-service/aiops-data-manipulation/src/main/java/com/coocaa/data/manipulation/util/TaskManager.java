package com.coocaa.data.manipulation.util;

import com.coocaa.core.tool.utils.SpringUtil;
import com.coocaa.data.manipulation.service.PromQLService;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * @program: intelligent_maintenance
 * @description: 定时任务管理
 * @author: dongyang_wu
 * @create: 2019-08-01 13:55
 */
@Component("TaskManager")
public class TaskManager implements DisposableBean {
    private final Map<Integer, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>(16);
    @Autowired
    private TaskScheduler taskScheduler;
    private static PromQLService promQLService;

    public void addCronTask(Integer taskId, Runnable task, String cronExpression) {
        if (this.scheduledTasks.containsKey(taskId)) {
            removeCronTask(taskId);
        }
        ScheduledFuture<?> future = taskScheduler.schedule(task, new CronTrigger(cronExpression));
        this.scheduledTasks.put(taskId, future);
    }

    public void removeCronTask(Integer taskId) {
        ScheduledFuture<?> scheduledTask = this.scheduledTasks.remove(taskId);
        if (scheduledTask != null && !scheduledTask.isCancelled()) {
            scheduledTask.cancel(true);
            System.out.println("删除定时任务");
        }
    }

    @Override
    public void destroy() {
        for (ScheduledFuture<?> scheduledTask : this.scheduledTasks.values()) {
            scheduledTask.cancel(true);
        }
        this.scheduledTasks.clear();
    }

    public static PromQLService getPromQLService() {
        //第一重判断
        if (promQLService == null) {
            //锁定代码块
            synchronized (PromQLService.class) {
                //第二重判断
                if (promQLService == null) {
                    promQLService = SpringUtil.getBean("PromQLService");
                }
            }
        }
        return promQLService;
    }
}