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
    private Long id;

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
     * 模型名
     */
    private String modelName;
    /**
     * 定时任务所属指标
     */
    private Long metricsId;
    /**
     * 所属Team
     */
    private String teamIds;
    private QueryRange queryRange;
}