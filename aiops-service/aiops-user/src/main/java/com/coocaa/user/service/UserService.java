package com.coocaa.user.service;

import com.coocaa.user.entity.User;
import com.coocaa.core.mybatis.base.BaseService;
import com.coocaa.user.entity.UserInfo;

import java.util.List;

/**
 * @program: intelligent_maintenance
 * @description:
 * @author: dongyang_wu
 * @create: 2019-07-29 14:48
 */
public interface UserService extends BaseService<User> {
    UserInfo userInfo(String account, String password);

    User getUser();
}