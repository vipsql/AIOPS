package com.coocaa.prometheus.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coocaa.common.constant.Constant;
import com.coocaa.common.constant.TableConstant;
import com.coocaa.common.request.RequestBean;
import com.coocaa.core.tool.utils.SqlUtil;
import com.coocaa.prometheus.entity.Task;
import com.coocaa.prometheus.mapper.TaskMapper;
import com.coocaa.prometheus.service.TaskService;
import com.coocaa.prometheus.util.TaskManager;
import com.coocaa.prometheus.util.runnable.QueryMetricTask;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @program: intelligent_maintenance
 * @description: 定时任务调度类
 * @author: dongyang_wu
 * @create: 2019-08-01 13:46
 */
@Service
@AllArgsConstructor
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements TaskService {
    private TaskManager taskManager;
    private TaskMapper taskMapper;

    @Override
    public Boolean createQueryMetricsTask(Task task, Integer type) {
        if (Constant.NumberType.ZERO_PROPERTY.equals(type)) {
            task.setArgs(JSON.toJSONString(task.getQueryInstant()));
        } else if (Constant.NumberType.ONE_PROPERTY.equals(type)) {
            task.setArgs(JSON.toJSONString(task.getQueryRange()));
        }
        task.setStatus(Constant.NumberType.GOOD_PROPERTY);
        task.setType(type);
        boolean insert = task.insertOrUpdate();
        if (insert) {
            QueryMetricTask queryMetricTask = new QueryMetricTask(task, type);
            taskManager.addCronTask(task.getTaskId(), queryMetricTask, task.getTaskCron());
        }
        return insert;
    }

    @Override
    @Transactional
    public Boolean removeQueryMetricsTask(RequestBean requestbean, Integer type) {
        if (Constant.NumberType.TWO_PROPERTY.equals(type)) {
            requestbean.getItems().forEach(item -> {
                List<Task> tasks = taskMapper.selectByMap(SqlUtil.map(item.getQuery(), item.getQueryString()).build());
                // 禁用指定的定时任务
                tasks.forEach(task -> {
                    task.setStatus(Constant.NumberType.BAD_PROPERTY);
                    task.insertOrUpdate();
                });
            });
        }
        Set<Integer> taskIdSets = new HashSet<>();
        requestbean.getItems().forEach(item -> {
            List<Task> tasks = taskMapper.selectByMap(SqlUtil.map(item.getQuery(), item.getQueryString()).build());
            List<Integer> ids = tasks.stream().map(Task::getTaskId).collect(Collectors.toList());
            taskIdSets.addAll(ids);
        });
        boolean deleteFlag = Constant.NumberType.ZERO_PROPERTY.equals(type);
        taskIdSets.forEach(item -> {
            taskManager.removeCronTask(item);
            if (deleteFlag) {
                taskMapper.deleteByMap(SqlUtil.map(TableConstant.TASK.TASK_ID, item.toString()).build());
            }
        });
        return true;
    }

    @Override
    public void bootstrapAllTask() {
        List<Task> tasks = taskMapper.findAll();
        tasks.forEach(task -> {
            QueryMetricTask queryMetricTask = new QueryMetricTask(task, task.getType());
            taskManager.addCronTask(task.getTaskId(), queryMetricTask, task.getTaskCron());
        });
    }

    @Override
    public boolean updateCron(Integer id, String cron) {
        return taskMapper.updateCronById(id, cron);
    }

    @Override
    public IPage<Task> searchAllTaskCron(Page page) {
        return taskMapper.selectAll(page);
    }

}