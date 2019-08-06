package com.coocaa.user.input;

import lombok.*;

import javax.validation.constraints.NotEmpty;

/**
 * @description: 用户输入实体类
 * @author: dongyang_wu
 * @create: 2019-08-06 11:51
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInputVo {
    private Long id;
    private String company;
    private String department;
    private String departmentGroup;
    private String organization;
    private String mobile;
    private String mail;
    private String name;
    private String account;
    private String teamIds;
    private String wechat;
}