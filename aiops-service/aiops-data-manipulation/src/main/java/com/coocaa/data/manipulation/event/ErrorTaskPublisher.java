package com.coocaa.data.manipulation.event;

import com.coocaa.core.tool.utils.*;
import com.coocaa.data.manipulation.constant.EventConstant;
import com.coocaa.data.manipulation.entity.TaskError;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: intelligent_maintenance
 * @description:
 * @author: dongyang_wu
 * @create: 2019-08-01 16:25
 */
public class ErrorTaskPublisher {

    public static void publishEvent(Integer taskId, Throwable error) {
        TaskError taskError = new TaskError();
        if (Func.isNotEmpty(error)) {
            taskError.setStackTrace(Exceptions.getStackTraceAsString(error));
            taskError.setExceptionName(error.getClass().getName());
            taskError.setMessage(error.getMessage());
            StackTraceElement[] elements = error.getStackTrace();
            if (Func.isNotEmpty(elements)) {
                StackTraceElement element = elements[0];
                taskError.setFileName(element.getFileName());
                taskError.setLineNumber(element.getLineNumber());
            }
        }
        Map<String, Object> event = new HashMap<>(16);
        event.put(EventConstant.EVENT_TASK, taskError);
        event.put(EventConstant.EVENT_TASK_ID, taskId);
        SpringUtil.publishEvent(new TaskErrorEvent(event));
    }
}