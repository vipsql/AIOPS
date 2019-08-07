package com.coocaa.prometheus.entity;

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
    @TableId(value = "id", type = IdType.AUTO)
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

    private Integer type;
    /**
     * 启动者
     */
    private Long createUserId;
    @NotEmpty(message = "定时任务参数不能为空")
    /**
     * 定时任务参数
     */
    private String args;
    /**
     * 是否启用0禁用 1启用
     */
    private Byte status;
    /**
     * 定时任务所属指标ID
     */
    private Long metricsId;
    /**
     * 指标选用模型名
     */
    private String modelName;
    @TableField(exist = false)
    private QueryInstant queryInstant;
    @TableField(exist = false)
    private QueryRange queryRange;
    @TableLogic
    private Integer logic;
    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
