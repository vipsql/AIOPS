package com.coocaa.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @program: intelligent_maintenance
 * @description: 机器表
 * @author: dongyang_wu
 * @create: 2019-07-31 10:49
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Machine extends Model<Machine> {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String ip;
    private Integer port;
    private Long user_id;
    private String metrics;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date createTime;
    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}