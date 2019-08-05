package com.coocaa.user.input;

import lombok.*;

import javax.validation.constraints.NotBlank;

/**
 * @description: 登录用户
 * @author: dongyang_wu
 * @create: 2019-07-30 21:23
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserVo {
    @NotBlank(message = "用户名不能为空")
    private String userName;
    @NotBlank(message = "密码不能为空")
    private String password;
}