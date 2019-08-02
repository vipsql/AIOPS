package com.coocaa.data.manipulation.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 定时任务信息
 * </p>
 *
 * @author wyx
 * @since 2019-07-25
 */
@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Task extends Model<Task> {

    private static final long serialVersionUID = 1L;

    /**
     * 任务 ID
     */
    @TableId(value = "task_id", type = IdType.AUTO)
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

    @NotEmpty(message = "查询指标不能为空")
    private String queryMetric;

    private Long createUserId;
    @NotEmpty(message = "定时任务参数不能为空")
    private String args;
    private Long machineId;
    @TableField(exist = false)
    private QueryInstant queryInstant;
    @TableField(exist = false)
    private QueryRange queryRange;
    private Byte status;

    @TableLogic
    private Integer logic;

    @Override
    protected Serializable pkVal() {
        return this.taskId;
    }

}
