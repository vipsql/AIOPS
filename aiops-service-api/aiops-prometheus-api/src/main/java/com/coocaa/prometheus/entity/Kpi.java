package com.coocaa.prometheus.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.Date;

/**
 * @description: KPI指标清单实体
 * @author: dongyang_wu
 * @create: 2019-08-08 09:31
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Kpi extends Model<Kpi> {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String name;
    private String promExpression;
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
}