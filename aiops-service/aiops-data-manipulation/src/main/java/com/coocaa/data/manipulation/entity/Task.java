package com.monitoring.data_manipulation.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

/**
 * <p>
 *  定时任务信息
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

    @TableLogic
    private Integer logic;

    @Override
    protected Serializable pkVal() {
        return this.taskId;
    }

}
