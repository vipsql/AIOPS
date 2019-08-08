package com.coocaa.user.feign;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coocaa.common.constant.TableConstant;
import com.coocaa.core.tool.api.*;
import com.coocaa.user.entity.*;
import com.coocaa.user.service.TeamService;
import com.coocaa.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.*;

/**
 * 用户服务Feign实现类
 *
 * @author dongyang_wu
 */
@RestController
@AllArgsConstructor
@ApiIgnore
public class UserClient implements IUserClient {

    private UserService userService;
    private TeamService teamService;

    @Override
    public R<UserInfo> userInfo(String account, String password) {
        return R.data(userService.userInfo(account, password));
    }

    @Override
    public R<User> userByMail(String mail) {
        User user = userService.getBaseMapper().selectOne(new QueryWrapper<User>().eq(TableConstant.USER.MAIL, mail));
        if (user != null) {
            return R.data(user);
        }
        return R.fail(ResultCode.ENTITY_NOT_EXIST);
    }

    @Override
    public R<User> userById(Long id) {
        return R.data(userService.getBaseMapper().selectById(id));
    }

    @Override
    @PostMapping(API_PREFIX + "/insert")
    public R<Integer> insert(User user) {
        int flag = 0;
        try {
            flag = userService.getBaseMapper().insert(user);
        } catch (Exception e) {
        }
        return R.data(flag);
    }

    public R<Set<User>> getTeamUsers(String teamIds, String connection) {
        return R.data(teamService.getTeamUsers(Arrays.asList(teamIds.split(" ")), connection));
    }
}
