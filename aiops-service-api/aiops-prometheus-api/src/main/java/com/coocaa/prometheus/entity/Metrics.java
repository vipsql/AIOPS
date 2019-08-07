package com.coocaa.prometheus.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private Integer logic;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}