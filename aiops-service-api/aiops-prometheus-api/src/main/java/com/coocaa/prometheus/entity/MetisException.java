package com.coocaa.prometheus.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel
public class MetisException extends Model<MetisException> {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    @ApiModelProperty("异常所属指标Id")
    private Long taskId;
    @ApiModelProperty("最近处理人用户")
    private Long recentUserId;
    @ApiModelProperty("异常数据List JSON字符串")
    private String matrixDataJson;
    @ApiModelProperty("0未处理1已修正2已恢复3已修复")
    private Integer status;
    @ApiModelProperty("处理人及理由JSON字符串")
    private String userToReasonJson;
    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    @ApiModelProperty("创建时间")
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