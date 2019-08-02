package com.coocaa.user.feign;


import com.coocaa.user.entity.User;
import com.coocaa.core.secure.constant.AppConstant;
import com.coocaa.core.tool.api.R;
import com.coocaa.user.entity.UserInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping(API_PREFIX + "/insert")
    R<Integer> insert(@RequestBody User user);

}
