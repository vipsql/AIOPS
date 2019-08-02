package com.coocaa.prometheus.event;

import com.coocaa.prometheus.constant.EventConstant;
import com.coocaa.prometheus.entity.TaskError;
import com.coocaa.prometheus.util.TaskManager;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @program: intelligent_maintenance
 * @description: 异常事件监听
 * @author: dongyang_wu
 * @create: 2019-08-01 16:18
 */
@Slf4j
@AllArgsConstructor
public class TaskErrorListener {

    @Async
    @Order
    @EventListener(TaskErrorEvent.class)
    public void saveErrorTask(TaskErrorEvent event) {
        Map<String, Object> source = (Map<String, Object>) event.getSource();
        TaskError taskError = (TaskError) source.get(EventConstant.EVENT_TASK);
        System.out.println(taskError);
        // 发邮件 // 记日志
    }
}