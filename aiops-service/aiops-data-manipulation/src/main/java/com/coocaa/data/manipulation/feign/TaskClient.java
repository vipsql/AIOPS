package com.coocaa.data.manipulation.feign;

import com.alibaba.fastjson.JSON;
import com.coocaa.common.constant.Constant;
import com.coocaa.common.request.RequestBean;
import com.coocaa.core.tool.api.R;
import com.coocaa.data.manipulation.entity.QueryInstant;
import com.coocaa.data.manipulation.entity.Task;
import com.coocaa.data.manipulation.util.TaskManager;
import com.coocaa.data.manipulation.util.runnable.QueryMetricTask;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: intelligent_maintenance
 * @description: 远程定时任务实现类
 * @author: dongyang_wu
 * @create: 2019-08-01 13:42
 */
@RestController
@AllArgsConstructor
public class TaskClient implements ITaskClient {
    private TaskManager taskManager;

    @Override
    public R<Boolean> createTask(Task task, Integer type) {
        if (Constant.NumberType.ZERO_PROPERTY.equals(type)) {
            task.setArgs(JSON.toJSONString(task.getQueryInstant()));
        } else if (Constant.NumberType.ONE_PROPERTY.equals(type)) {
            task.setArgs(JSON.toJSONString(task.getQueryRange()));
        }
        boolean insert = task.insertOrUpdate();
        // System.out.println(JSON.parseObject(task.getArgs(), QueryInstant.class));
        if (insert) {
            QueryMetricTask queryMetricTask = new QueryMetricTask(task, type);
            taskManager.addCronTask(task.getTaskId(), queryMetricTask, task.getTaskCron());
        }
        return R.data(insert);
    }

    @Override
    public R<Boolean> removeTask(RequestBean requestbean, Integer type) {
        return null;
    }
}