package com.coocaa.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coocaa.common.constant.TableConstant;
import com.coocaa.common.request.RequestBean;
import com.coocaa.core.secure.utils.SecureUtil;
import com.coocaa.core.tool.utils.*;
import com.coocaa.user.entity.User;
import com.coocaa.user.input.UserInputVo;
import com.coocaa.user.mapper.TeamMapper;
import com.coocaa.user.mapper.UserMapper;
import com.coocaa.user.service.UserService;
import com.coocaa.core.log.exception.ApiException;
import com.coocaa.core.log.exception.ApiResultEnum;
import com.coocaa.core.mybatis.base.BaseServiceImpl;
import com.coocaa.user.entity.UserInfo;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @description: 用户控制器
 * @author: dongyang_wu
 * @create: 2019-07-29 14:51
 */
@Service("UserService")
@AllArgsConstructor
public class UserServiceImpl extends BaseServiceImpl<UserMapper, User> implements UserService {
    private UserMapper userMapper;
    private TeamMapper teamMapper;

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

    @Override
    public User insertOrUpdate(UserInputVo userInputVo) {
        User user = new User();
        BeanUtils.copyProperties(userInputVo, user);
        Long userId = user.getId();
        Long currentUserId = SecureUtil.getUserId();
        // 修改权限判断
        if (userId != null && userId != 0 && !currentUserId.equals(userId)) {
            User databaseUser = userMapper.selectById(userId);
            // 修改的用户所在Team的所有管理员Id
            List<String> adminUserIds = teamMapper.selectAdminUserIdsInTeamIds(TableConstant.ID, StringUtil.addBrackets(databaseUser.getTeamIds()));
            // 判断当前用户Id是否在user的Team管理员Id里
            if (CollectionUtil.isEmpty(adminUserIds) || !adminUserIds.contains(currentUserId + "")) {
                throw new ApiException(ApiResultEnum.USER_NOT_USER_TEAM_ADMIN);
            }
            System.out.println(adminUserIds);
        }
        user.insertOrUpdate();
        return fillUserTeams(user);
    }

    @Override
    public User fillUserTeams(User user) {
        String teamIds = user.getTeamIds();
        if (!StringUtil.isEmpty(teamIds)) {
            user.setTeams(teamMapper.selectByIdInUser(StringUtil.addBrackets(teamIds)));
        }
        return user;
    }

}