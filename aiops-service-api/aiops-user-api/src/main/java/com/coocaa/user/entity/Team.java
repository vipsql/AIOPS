package com.coocaa.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @description: Team
 * @author: dongyang_wu
 * @create: 2019-08-05 16:14
 */
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Team extends Model<Team> {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String name;
    private Long adminUserId;
//    @TableField(exist = false)
//    private User adminUser;
    @TableField(exist = false)
    private List<User> userList;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}