package com.monitoring.data_manipulation.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.monitoring.data_manipulation.entity.Task;
import com.monitoring.data_manipulation.mapper.task.TaskMapper;
import com.monitoring.data_manipulation.service.ITaskService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wyx
 * @since 2019-07-25
 */
@Service
public class TaskServiceImpl extends ServiceImpl<TaskMapper, Task> implements ITaskService {

    @Autowired
    private TaskMapper taskMapper;

    @Override
    public boolean updateCron(Integer id, String cron) {
        return taskMapper.updateCronById(id, cron);
    }

    @Override
    public IPage<Task> searchAllTaskCron(Page page) {
        return taskMapper.selectAll(page);
    }
}
