package com.coocaa.prometheus.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

import java.io.Serializable;
import java.util.Date;


/**
 * @description: 指标表
 * @author: dongyang_wu
 * @create: 2019-08-05 15:18
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Metrics extends Model<Metrics> {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String metricName;
    /**
     * team id集合
     */
    private String teamIds;
    /**
     * model id集合
     */
    private String modelIds;
    /**
     * 指标所对应的定时任务Id
     */
    private Integer taskId;

    /**
     * 查询条件JSON字符串
     */
    private String queryRangeJson;
    /**
     * 定时拉取数据频率
     */
    private String taskCron;
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
    @TableLogic
    private Integer logic;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}