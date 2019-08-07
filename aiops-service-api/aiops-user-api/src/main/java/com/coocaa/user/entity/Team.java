package com.coocaa.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

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
    @TableLogic
    @JsonIgnore
    private Integer logic;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    //    @TableField(exist = false)
//    private User adminUser;
//    @TableField(exist = false)
//    private List<User> userList;
    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}