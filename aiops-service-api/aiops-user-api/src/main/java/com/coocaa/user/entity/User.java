package com.coocaa.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

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
    @NotEmpty(message = "密码不能为空")
    private String password;
    private String salt;
    private String teamIds;
    @TableLogic
    private Integer logic;
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    @Version
    private Integer version;

    @TableField(exist = false)
    private List<Machine> machines;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}