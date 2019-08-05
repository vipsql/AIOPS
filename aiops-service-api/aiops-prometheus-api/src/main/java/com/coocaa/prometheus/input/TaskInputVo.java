package com.coocaa.prometheus.input;

import com.coocaa.prometheus.entity.QueryRange;
import lombok.*;

import javax.validation.constraints.NotEmpty;

/**
 * @description: 定时任务输入实体类
 * @author: dongyang_wu
 * @create: 2019-08-05 13:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaskInputVo {
    /**
     * 任务 ID
     */
    private Integer taskId;

    /**
     * 任务名
     */
    @NotEmpty(message = "任务名不能为空")
    private String taskName;
    /**
     * 任务描述
     */
    @NotEmpty(message = "任务描述不能为空")
    private String taskDescription;

    /**
     * 计划任务定时
     */
    @NotEmpty(message = "定时计划不能为空")
    private String taskCron;
    /**
     * 查询指标描述
     */
    @NotEmpty(message = "查询指标不能为空")
    private String queryMetric;
    /**
     * 客户端订阅URL
     */
    @NotEmpty(message = "客户端订阅URL")
    private String subscribeName;
    /**
     * 定时任务类型
     */
    private Integer type;
    private QueryRange queryRange;
}