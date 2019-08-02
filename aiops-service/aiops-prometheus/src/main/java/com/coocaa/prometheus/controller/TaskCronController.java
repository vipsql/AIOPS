package com.coocaa.prometheus.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coocaa.core.tool.response.ReturnData;
import com.coocaa.core.tool.utils.BindingResultUtil;
import com.coocaa.core.tool.utils.HelpUtil;
import com.coocaa.prometheus.entity.Task;
import com.coocaa.prometheus.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author wyx
 * @since 2019-07-25
 */
@RestController
@RequestMapping("/taskCron")
@AllArgsConstructor
public class TaskCronController {

    private TaskService taskCronService;

    @PostMapping("/addTask")
    public ReturnData addTask(@RequestBody @Valid Task task, BindingResult br) {
        if (br.hasErrors()) {
            BindingResultUtil.logError(br);
        }
        boolean result = taskCronService.save(task);
        return HelpUtil.checkDBResult(result);
    }

    @GetMapping("/deleteTask/{id}")
    public ReturnData deleteTask(@PathVariable("id") Integer id) {
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        boolean result = taskCronService.remove(queryWrapper.eq("task_id", id));

        return HelpUtil.checkDBResult(result);
    }

    @GetMapping("/searchTaskById/{id}")
    public ReturnData searchTaskById(@PathVariable("id") Integer id) {
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        Task task = taskCronService.getOne(queryWrapper.eq("task_id", id));

        return HelpUtil.checkEmpty(task);
    }

    @GetMapping("/searchAllTasks/{current}/{size}")
    public ReturnData searchAllTasks(@PathVariable(value = "current") Integer current, @PathVariable(value = "size") Integer size) {
        Page<Task> page = new Page<>();
        page.setCurrent(current);
        page.setSize(size);

        IPage<Task> taskCronList = taskCronService.searchAllTaskCron(page);

        return HelpUtil.checkEmpty(taskCronList);
    }

    @PostMapping("/updateCron")
    public ReturnData updateCron(@RequestBody Task task) {
        boolean result = taskCronService.updateCron(task.getTaskId(), task.getTaskCron());

        return HelpUtil.checkDBResult(result);
    }

}
