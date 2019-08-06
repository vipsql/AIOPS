package com.coocaa.user.service;

import com.coocaa.common.request.RequestBean;
import com.coocaa.user.entity.User;
import com.coocaa.core.mybatis.base.BaseService;
import com.coocaa.user.entity.UserInfo;
import com.coocaa.user.input.UserInputVo;

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

    List<User> getByAttr(RequestBean requestBean);

    User insertOrUpdate(UserInputVo userInputVo);

    User fillUserTeams(User user);

}