package com.coocaa.user.feign;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coocaa.common.constant.TableConstant;
import com.coocaa.core.tool.api.*;
import com.coocaa.user.entity.User;
import com.coocaa.user.entity.UserInfo;
import com.coocaa.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户服务Feign实现类
 *
 * @author dongyang_wu
 */
@RestController
@AllArgsConstructor
public class UserClient implements IUserClient {

    UserService userService;

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
    @PostMapping(API_PREFIX + "/insert")
    public R<Integer> insert(User user) {
        int flag = 0;
        try {
            flag = userService.getBaseMapper().insert(user);
        } catch (Exception e) {
        }
        return R.data(flag);
    }
}
