package com.coocaa.prometheus.system;

import com.coocaa.prometheus.util.TaskManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ScheduledFuture;

/**
 * @author: dongyang_wu
 * @create: 2019-08-27 16:25
 * @description: 应用停止监听
 */
@Component
@AllArgsConstructor
@Slf4j
public class SystemExit implements ApplicationListener<ApplicationEvent> {
    private TaskManager taskManager;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextClosedEvent) {
            Map<Long, ScheduledFuture<?>> scheduledTasks = taskManager.getScheduledTasks();
            if (scheduledTasks.size() == 0)
                return;
            scheduledTasks.forEach((key, value) -> taskManager.removeCronTask(key));
            log.info("停止所有定时任务");
        }
    }
}