package com.coocaa.prometheus.system;

import com.coocaa.prometheus.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @description: 系统初始化启动定时任务
 * @author: dongyang_wu
 * @create: 2019-08-01 20:00
 */
@Component
@AllArgsConstructor
public class SystemInit implements ApplicationRunner {
    private TaskService taskService;

    @Override
    public void run(ApplicationArguments args) {
        taskService.bootstrapAllTask();
    }
}