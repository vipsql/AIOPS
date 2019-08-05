package com.monitoring.data_manipulation.config;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coocaa.common.util.DateUtil;
import com.monitoring.data_manipulation.entity.PromMatrixData;
import com.monitoring.data_manipulation.entity.Task;
import com.monitoring.data_manipulation.mapper.task.TaskMapper;
import com.monitoring.data_manipulation.rabbitMQ.TimingDataSender;
import com.monitoring.data_manipulation.service.NormSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Auther: wyx
 * @Date: 2019-07-25 14:43
 * @Description: 动态定时任务配置
 */
@Configuration
@EnableScheduling
public class ScheduleConfig implements SchedulingConfigurer {

    @Autowired
    private TaskMapper taskMapper;

    @Autowired
    private NormSearchService normSearchService;

    @Autowired
    private TimingDataSender sender;

    private final String defaultCron = "0/15 * * * * ?";

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        Integer PromTaskId = 1;
        taskRegistrar.addTriggerTask(getTask(PromTaskId), getTrigger(getCron(PromTaskId)));
    }


    private Runnable getTask(Integer taskId){
        if(taskId == 1){
            return new Runnable() {
                @Override
                public void run() {
                    Date now = new Date();
                    Date start = DateUtil.getBeforeDate(now, 0, 0, 0, 15);
                    Date end = DateUtil.getAfterDate(now, 0, 0, 0, 0, 0, 0);
                    PromMatrixData promMatrixData = normSearchService.queryRangeData(start, end, 5);
                    String jsonData = JSON.toJSONString(promMatrixData);
                    sender.send(jsonData);
                }
            };
        }
        return new Runnable() {
            @Override
            public void run() {
                System.out.println("定期任务: " + LocalDateTime.now().toLocalTime());
            }
        };
    }

    private Trigger getTrigger(String cron){
        return new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                CronTrigger trigger = new CronTrigger(cron);
                return trigger.nextExecutionTime(triggerContext);
            }
        };
    }

    private String getCron(Integer id){
        QueryWrapper<Task> queryWrapper = new QueryWrapper<>();
        Task task = taskMapper.selectOne(queryWrapper.eq("task_id", id));
        return task == null ? defaultCron : task.getTaskCron();
    }
}
