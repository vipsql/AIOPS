package com.coocaa.user.feign;


import com.coocaa.user.entity.User;
import com.coocaa.core.secure.constant.AppConstant;
import com.coocaa.core.tool.api.R;
import com.coocaa.user.entity.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * User Feign接口类
 *
 * @author dongyang_wu
 */
@FeignClient(
        value = AppConstant.APPLICATION_USER_NAME
)
public interface IUserClient {

    String API_PREFIX = "/user";

    /**
     * 获取用户信息
     *
     * @param account  账号
     * @param password 密码
     * @return
     */
    @GetMapping(API_PREFIX + "/user-info")
    R<UserInfo> userInfo(@RequestParam("account") String account, @RequestParam("password") String password);

    @GetMapping(API_PREFIX + "/userByMail")
    R<User> userByMail(@RequestParam("mail") String mail);

    @GetMapping(API_PREFIX + "/userById")
    R<User> userById(@RequestParam("id") Long id);

    @PostMapping(API_PREFIX + "/insert")
    R<Integer> insert(@RequestBody User user);

    /**
     * 获取Team下的所有User
     *
     * @param teamIds
     * @param connection
     * @return
     */
    @GetMapping(API_PREFIX + "/getTeamUsers")
    R<Set<User>> getTeamUsers(@RequestParam("teamIds") String teamIds, @RequestParam("connection") String connection);

}
