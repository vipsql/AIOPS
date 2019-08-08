package com.coocaa.prometheus.input;

import com.coocaa.prometheus.entity.QueryRange;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel
public class TaskInputVo {
    @ApiModelProperty("任务 ID")
    private Long id;
    @NotEmpty(message = "任务名不能为空")
    @ApiModelProperty("任务名")
    private String taskName;
    @NotEmpty(message = "任务描述不能为空")
    @ApiModelProperty("任务描述")
    private String taskDescription;
    @NotEmpty(message = "定时计划不能为空")
    @ApiModelProperty("计划任务定时")
    private String taskCron;
    @ApiModelProperty("定时任务选用模型名")
    private String modelName;
    @ApiModelProperty("定时任务所属指标集")
    private Long metricsId;
    @ApiModelProperty("定时任务所属Team")
    private String teamIds;
    @ApiModelProperty("定时任务参数--其中span主要用异常展示")
    private QueryRange queryRange;
}