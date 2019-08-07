package com.coocaa.prometheus.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coocaa.common.constant.Constant;
import com.coocaa.common.constant.TableConstant;
import com.coocaa.common.request.RequestBean;
import com.coocaa.core.mybatis.base.BaseServiceImpl;
import com.coocaa.core.tool.utils.SqlUtil;
import com.coocaa.prometheus.entity.QueryRange;
import com.coocaa.prometheus.entity.Task;
import com.coocaa.prometheus.input.TaskInputVo;
import com.coocaa.prometheus.mapper.TaskMapper;
import com.coocaa.prometheus.service.TaskService;
import com.coocaa.prometheus.util.TaskManager;
import com.coocaa.prometheus.util.runnable.QueryMetricTask;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
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
public class TaskServiceImpl extends BaseServiceImpl<TaskMapper, Task> implements TaskService {
    private TaskManager taskManager;
    private TaskMapper taskMapper;

    @Override
    public Task createQueryMetricsTask(TaskInputVo taskInputVo) {
        Task task = new Task();
        BeanUtils.copyProperties(taskInputVo, task);
        QueryRange queryRange = task.getQueryRange();
        if (queryRange != null)
            task.setArgs(JSON.toJSONString(queryRange));
        task.setStatus(Constant.NumberType.GOOD_PROPERTY);
        boolean insert = task.insertOrUpdate();
        if (insert) {
            QueryMetricTask queryMetricTask = new QueryMetricTask(task, task.getType() == null ? 0 : task.getType());
            taskManager.addCronTask(task.getId(), queryMetricTask, task.getTaskCron());
        }
        return task;
    }

    @Override
    @Transactional
    public Boolean removeQueryMetricsTask(RequestBean requestbean, Integer type) {
        Set<Long> taskIdSets = new HashSet<>();
        requestbean.getItems().forEach(item -> {
            List<Task> tasks = taskMapper.selectByMap(SqlUtil.map(item.getQuery(), item.getQueryString()).build());
            if (Constant.NumberType.TWO_PROPERTY.equals(type)) {
                // 禁用指定的定时任务
                tasks.forEach(task -> {
                    task.setStatus(Constant.NumberType.BAD_PROPERTY);
                    task.insertOrUpdate();
                });
            }
            // 获取指定的sets
            List<Long> ids = tasks.stream().map(Task::getId).collect(Collectors.toList());
            taskIdSets.addAll(ids);
        });
        boolean deleteFlag = Constant.NumberType.ZERO_PROPERTY.equals(type);
        taskIdSets.forEach(item -> {
            taskManager.removeCronTask(item);
            if (deleteFlag) {
                taskMapper.deleteByMap(SqlUtil.map(TableConstant.ID, item.toString()).build());
            }
        });
        return true;
    }

    @Override
    public void bootstrapAllTask() {
        List<Task> tasks = taskMapper.findAll();
        tasks.forEach(task -> {
            QueryMetricTask queryMetricTask = new QueryMetricTask(task, task.getType());
            taskManager.addCronTask(task.getId(), queryMetricTask, task.getTaskCron());
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