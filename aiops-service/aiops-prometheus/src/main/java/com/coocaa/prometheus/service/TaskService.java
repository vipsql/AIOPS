package com.coocaa.prometheus.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.coocaa.common.request.RequestBean;
import com.coocaa.core.mybatis.base.BaseService;
import com.coocaa.prometheus.entity.Task;
import com.coocaa.prometheus.input.MetisCsvInputVo;
import com.coocaa.prometheus.input.TaskInputVo;
import com.coocaa.prometheus.output.MetricsCsvVo;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @program: intelligent_maintenance
 * @description: 定时任务调度类
 * @author: dongyang_wu
 * @create: 2019-08-01 13:45
 */
public interface TaskService extends BaseService<Task> {
    /**
     * 新增或更新定时任务
     */
    Task createQueryMetricsTask(TaskInputVo taskInputVo);

    /**
     * 停止、删除或禁用定时任务
     */
    Boolean removeQueryMetricsTask(RequestBean requestbean, Integer type);

    /**
     * 启动所有定时任务
     */
    void bootstrapAllTask();
    /**
     * 导出对应指标的Csv训练数据
     */
    List<MetricsCsvVo> exportMetisCsv(MetisCsvInputVo metisCsvInputVo) throws ExecutionException, InterruptedException;

    /**
     * 导出数据并传入Metis进行训练
     * @param metisCsvInputVo
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    JSONObject exportMetisCsvToTrain(MetisCsvInputVo metisCsvInputVo) throws ExecutionException, InterruptedException;

    /**
     * 重新启动定时任务
     */
    void restartTask(RequestBean requestbean);

}