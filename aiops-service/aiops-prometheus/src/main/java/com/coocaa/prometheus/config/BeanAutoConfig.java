package com.coocaa.prometheus.config;

import com.coocaa.prometheus.event.TaskErrorListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

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
}