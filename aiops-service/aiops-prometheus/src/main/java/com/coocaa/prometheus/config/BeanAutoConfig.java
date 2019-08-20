package com.coocaa.prometheus.config;

import com.coocaa.prometheus.event.TaskErrorListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.util.concurrent.Executor;

/**
 * @program: intelligent_maintenance
 * @description: Bean自动注入
 * @author: dongyang_wu
 * @create: 2019-08-01 20:35
 */
@Configuration
public class BeanAutoConfig {
    @Bean
    public TaskErrorListener taskErrorListener() {
        return new TaskErrorListener();
    }

    @Bean
    public TaskScheduler taskScheduler() {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        // 定时任务执行线程池核心线程数
        taskScheduler.setPoolSize(4);
        taskScheduler.setRemoveOnCancelPolicy(true);
        taskScheduler.setThreadNamePrefix("TaskSchedulerThreadPool-");
        return taskScheduler;
    }
}