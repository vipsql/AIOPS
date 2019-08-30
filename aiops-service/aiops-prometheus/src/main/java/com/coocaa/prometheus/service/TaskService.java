package com.coocaa.prometheus.service;

import com.coocaa.common.request.RequestBean;
import com.coocaa.core.mybatis.base.BaseService;
import com.coocaa.prometheus.entity.MatrixData;
import com.coocaa.prometheus.entity.Task;
import com.coocaa.prometheus.input.MetisCsvInputVo;
import com.coocaa.prometheus.input.TaskInputVo;
import com.coocaa.prometheus.output.MetisCsvOutputVo;
import com.coocaa.prometheus.output.MetricsCsvVo;

import java.util.*;
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
    Task createQueryMetricsTask(TaskInputVo taskInputVo, Integer type);

    /**
     * 停止、删除、禁用或启用定时任务
     */
    Boolean changeQueryMetricsTask(RequestBean requestbean, Integer type);

    /**
     * 启动所有定时任务
     */
    void bootstrapAllTask();

    /**
     * 导出对应指标的Csv训练数据
     */
    List<MetricsCsvVo> exportMetisCsv(MetisCsvInputVo metisCsvInputVo) throws ExecutionException, InterruptedException;

    /**
     * 导出指定时间段指定时间跨度的Metis训练数据并进行人工标注
     *
     * @param metisCsvInputVo
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    MetisCsvOutputVo exportMetisCsvToMark(MetisCsvInputVo metisCsvInputVo) throws ExecutionException, InterruptedException;

    /**
     * 将人工标注的数据导入Metis进行训练
     *
     * @param trainVos
     */
    void exportMetisCsvToTrain(MetisCsvOutputVo trainVos, String modelName);

    /**
     * 重新启动定时任务
     */
    void restartTask(RequestBean requestbean);

    /**
     * 将指标传入Metis进行检测
     *
     * @param task
     * @return
     */
    Map<String, MatrixData> detectByMetis(Task task, Date date) throws ExecutionException, InterruptedException;

}