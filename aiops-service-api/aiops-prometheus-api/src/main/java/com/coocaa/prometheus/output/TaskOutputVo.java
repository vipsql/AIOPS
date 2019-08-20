package com.coocaa.prometheus.output;

import com.baomidou.mybatisplus.annotation.*;
import com.coocaa.prometheus.entity.QueryRange;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.*;

/**
 * @author: dongyang_wu
 * @create: 2019-08-16 12:49
 * @description: 指标输出类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel
public class TaskOutputVo {
    @ApiModelProperty("主键")
    private Long id;
    @ApiModelProperty("指标名")
    private String taskName;
    @ApiModelProperty("任务描述")
    private String taskDescription;
    @ApiModelProperty("cron表达式")
    private String taskCron;
    @ApiModelProperty("创建者")
    private Map<Long, Object> createUserIdToNameMap;
    @ApiModelProperty("是否启用0禁用 1启用")
    private Byte status;
    @ApiModelProperty("定时任务所属指标集Id to Name名")
    private Map<Long, Object> metricsIdToNameMap;
    @ApiModelProperty("指标选用模型名")
    private String modelName;
    @ApiModelProperty("指标所属teamId to Name集合")
    private Map<Long, String> teamIdToNameMap;
    @ApiModelProperty("指标可选条件")
    @JsonIgnore
    private Map<String, Set<String>> conditionResult;
    @ApiModelProperty("指标当前条件")
    private QueryRange queryRange;
    private Date updateTime;
    private Date createTime;
}