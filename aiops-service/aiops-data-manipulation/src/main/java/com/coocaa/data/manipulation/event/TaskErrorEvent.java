package com.coocaa.data.manipulation.event;

import org.springframework.context.ApplicationEvent;

/**
 * @program: intelligent_maintenance
 * @description: 定时任务异常事件
 * @author: dongyang_wu
 * @create: 2019-08-01 16:17
 */
public class TaskErrorEvent extends ApplicationEvent {
    public TaskErrorEvent(Object source) {
        super(source);
    }
}