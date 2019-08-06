package com.coocaa.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;
import java.util.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends Model<User> {
    private static final long serialVersionUID = 1L;
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String company;
    private String department;
    private String departmentGroup;
    private String organization;
    private String mobile;
    private String mail;
    private String name;
    private String account;
    private String wechat;
    @JsonIgnore
    private String password;
    @JsonIgnore
    private String salt;
    private String teamIds;
    @TableField(exist = false)
    private List<Team> teams;
    @TableLogic
    @JsonIgnore
    private Integer logic;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    @Version
    @JsonIgnore
    private Integer version;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public boolean equals(Object a) {
        User user = (User) a;
        return this.getId().equals(user.getId());
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }
}