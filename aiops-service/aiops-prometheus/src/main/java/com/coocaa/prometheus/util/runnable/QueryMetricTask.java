package com.coocaa.prometheus.util.runnable;

import com.alibaba.fastjson.JSON;
import com.coocaa.common.constant.Constant;
import com.coocaa.core.log.exception.ApiException;
import com.coocaa.core.log.exception.ApiResultEnum;
import com.coocaa.core.tool.utils.*;
import com.coocaa.detector.entity.DetectorResult;
import com.coocaa.prometheus.entity.*;
import com.coocaa.prometheus.event.ErrorTaskPublisher;
import com.coocaa.prometheus.util.SingletonEnum;
import com.coocaa.prometheus.util.TaskManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 查询指标线程
 * @author: dongyang_wu
 * @create: 2019-08-01 14:12
 */
@Slf4j
public class QueryMetricTask implements Runnable {
    private Task task;

    public QueryMetricTask(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        Task realTask = SingletonEnum.INSTANCE.getTaskService().getById(task.getId());
        // 延时停止或更新定时任务
        preStopHandle(realTask, task);
        // 更新参数
        BeanUtil.copyProperties(realTask, task);
        try {
            Date now = new Date();
            // 将task传入Metis进行检测
            Map<String, MatrixData> rangeValues = SingletonEnum.INSTANCE.getTaskService().detectByMetis(task, now);
            Map<String, MatrixData> errorItems = new ConcurrentHashMap<>(16);
            // 找出异常列表
            rangeValues.forEach((key, value) -> {
                DetectorResult.DataBean detectResult = value.getDetectResult();
                // 没有匹配的dataA、dataB、dataC
                if (detectResult == null)
                    return;
                if (detectResult.getRet() == Constant.MetisException.RET || Double.parseDouble(detectResult.getP()) < Constant.MetisException.P) {
                    errorItems.put(key, value);
                }
            });
            log.info("定时任务id:" + task.getId() + "  " + task.getTaskCron() + "    异常错误列表:" + errorItems.toString());
            // 异常任务参数不对，无法获取正常数据
            if (CollectionUtil.isEmpty(rangeValues))
                throw new ApiException(ApiResultEnum.CYCLE_JOB_ARGS_ERROR);
            // 没有出现异常
            if (CollectionUtils.isEmpty(errorItems))
                return;
            MetisException metisException = MetisException.builder()
                    .createTime(now)
                    .matrixDataJson(JSON.toJSONString(errorItems))
                    .status(Constant.NumberType.ZERO_PROPERTY)
                    .taskId(task.getId())
                    .build();
            metisException.insertOrUpdate();
            if (StringUtil.isEmpty(task.getTeamIds()))
                return;
            SingletonEnum.INSTANCE.getAsyncServiceTask().sendDetectResult(errorItems, task.getTeamIds(), task.getTaskName());
            // TaskManager.getTimingDataSender().send(JSONArray.toJSONString(rangeValues));
        } catch (Exception e) {
            log.info(e.toString());
            TaskManager taskManager = SingletonEnum.INSTANCE.getTaskManager();
            if (taskManager.isOverErrorTimes(task.getId())) {
                ErrorTaskPublisher.publishEvent(task.getTaskDescription(), e);
                taskManager.deleteErrorTimesMap(task.getId());
                task.setIsUp(Constant.NumberType.BAD_PROPERTY);
                task.setErrorNumber(task.getErrorNumber() != null ? task.getErrorNumber() + 1 : 1);
                // 更新定时任务
                task.updateById();
                // 删除定时任务
                task.deleteById();
                System.out.println("定时任务异常次数超过100次,即将退出....");
                // 最后停止线程
                taskManager.removeCronTask(task.getId());
                return;
            }
            task.setErrorNumber(task.getErrorNumber() != null ? task.getErrorNumber() + 1 : 1);
            task.updateById();
        }
    }

    private void preStopHandle(Task oriTask, Task realTask) {
        boolean isStopFlag = SingletonEnum.INSTANCE.getTaskManager().judgeTaskStopFlag(realTask, true);
        if (isStopFlag) {
            SingletonEnum.INSTANCE.getTaskManager().removeCronTask(oriTask.getId());
            log.info("定时任务id:" + task.getId() + "  状态发生变化,即将退出......");
            return;
        }
        // cron表达式发生变化
        if (!oriTask.getTaskCron().equalsIgnoreCase(realTask.getTaskCron())) {
            SingletonEnum.INSTANCE.getTaskManager().addCronTask(realTask);
            log.info("定时任务id:" + task.getId() + "   cron表达式发生变化,即将重启......");
        }
    }
}