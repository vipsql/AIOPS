package com.coocaa.prometheus.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @description: Metis异常列表数据库
 * @author: dongyang_wu
 * @create: 2019-08-07 09:24
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MetisException extends Model<MetisException> {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 异常所属指标Id
     */
    private Long metricsId;
    /**
     * 最近处理人用户
     */
    private Long recentUserId;

    /**
     * 异常数据List JSON字符串
     */
    private String matrixDataJson;
    /**
     * 0未处理1已修正2已恢复3已修复
     */
    private Integer status;
    /**
     * 处理人及理由JSON字符串
     */
    private String userToReasonJson;
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