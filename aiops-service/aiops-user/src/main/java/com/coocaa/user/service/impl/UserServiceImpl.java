package com.coocaa.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coocaa.common.request.RequestBean;
import com.coocaa.core.tool.utils.SqlUtil;
import com.coocaa.user.entity.User;
import com.coocaa.user.mapper.UserMapper;
import com.coocaa.user.service.UserService;
import com.coocaa.core.log.exception.ApiException;
import com.coocaa.core.log.exception.ApiResultEnum;
import com.coocaa.core.mybatis.base.BaseServiceImpl;
import com.coocaa.core.tool.utils.Func;
import com.coocaa.user.entity.UserInfo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: intelligent_maintenance
 * @description:
 * @author: dongyang_wu
 * @create: 2019-07-29 14:51
 */
@Service("UserService")
@AllArgsConstructor
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User> implements UserService {
    private UserMapper userMapper;

    @Override
    public UserInfo userInfo(String account, String password) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("account", account).eq("password", password));
        if (Func.isEmpty(user)) {
            throw new ApiException(ApiResultEnum.ENTITY_NOT_EXIST);
        }
        return UserInfo.builder().user(user).build();
    }

    @Override
    public User getUser() {
        return userMapper.getUser(1L);
    }

    @Override
    public List<User> getByAttr(RequestBean requestBean) {
        return userMapper.selectByMap(SqlUtil.map(requestBean));
    }
}