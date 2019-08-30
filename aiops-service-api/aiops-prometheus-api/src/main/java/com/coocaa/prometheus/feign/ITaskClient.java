package com.coocaa.prometheus.feign;

import com.coocaa.core.secure.constant.AppConstant;
import com.coocaa.core.tool.api.R;
import com.coocaa.prometheus.entity.Task;
import com.coocaa.prometheus.input.TaskInputVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description: 远程定时任务调用Feign
 * @author: dongyang_wu
 * @create: 2019-08-01 13:22
 */
@FeignClient(
        value = AppConstant.APPLICATION_TASK_NAME
)
public interface ITaskClient {
    String API_PREFIX = "/dataManipulation";

    /**
     * 新建定时任务
     *
     * @return
     */
    @GetMapping(API_PREFIX + "/create-task")
    R<Task> createTask(@RequestBody TaskInputVo task, @RequestParam Integer type);

    @GetMapping(API_PREFIX + "/remove-task")
    R<Boolean> removeTask(@RequestParam Long taskId, @RequestParam Integer type);

    @GetMapping(API_PREFIX + "/selectTaskByTeamIds")
    R<List<Task>> selectTaskByTeamIds(@RequestParam("teamIds") String teamIds);

    @GetMapping(API_PREFIX + "/deleteTeamIdFromTask")
    R<Boolean> deleteTeamIdFromTask(@RequestParam("teamIds") String teamId);
}