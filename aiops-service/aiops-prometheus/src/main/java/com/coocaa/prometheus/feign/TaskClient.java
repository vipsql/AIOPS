package com.coocaa.prometheus.feign;

import com.coocaa.common.constant.Constant;
import com.coocaa.core.tool.api.R;
import com.coocaa.prometheus.entity.Task;
import com.coocaa.prometheus.input.TaskInputVo;
import com.coocaa.prometheus.service.TaskService;
import com.coocaa.prometheus.util.TaskManager;
import lombok.AllArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @program: intelligent_maintenance
 * @description: 远程定时任务实现类
 * @author: dongyang_wu
 * @create: 2019-08-01 13:42
 */
@RestController
@AllArgsConstructor
@ApiIgnore
public class TaskClient implements ITaskClient {
    private TaskService taskService;
    private TaskManager taskManager;

    @Override
    public R<Task> createTask(TaskInputVo task, Integer type) {
        return R.data(taskService.createQueryMetricsTask(task));
    }

    @Override
    @Transactional
    public R<Boolean> removeTask(Long taskId, Integer type) {
        boolean removeResult = taskManager.removeCronTask(taskId);
        int deleteResult = 0;
        // 0删除1停止
        if (removeResult && Constant.NumberType.ZERO_PROPERTY.equals(type)) {
            deleteResult = taskService.getBaseMapper().deleteById(taskId);
        }
        return R.data(removeResult && deleteResult == 1);
    }
}