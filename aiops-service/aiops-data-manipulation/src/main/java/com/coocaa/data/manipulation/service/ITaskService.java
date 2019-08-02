package com.monitoring.data_manipulation.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.monitoring.data_manipulation.entity.Task;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wyx
 * @since 2019-07-25
 */
public interface ITaskService extends IService<Task> {

    /**
     * 根据 taskId 修改 task
     * @param id
     * @param cron
     * @return
     */
    boolean updateCron(Integer id, String cron);

    /**
     * 查询全部任务信息
     * @return
     */
    IPage<Task> searchAllTaskCron(Page page);

}
