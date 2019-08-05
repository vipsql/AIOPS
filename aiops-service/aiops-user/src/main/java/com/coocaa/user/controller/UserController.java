package com.coocaa.user.controller;

import com.coocaa.common.request.RequestBean;
import com.coocaa.user.service.UserService;
import com.coocaa.core.log.response.ResponseHelper;
import com.coocaa.core.log.response.ResultBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @description:
 * @author: dongyang_wu
 * @create: 2019-07-29 14:55
 */
@RestController
@Api(value = "用户模块", tags = "用户接口")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @PostMapping("/user/attr")
    @ApiOperation(value = "获取指定属性的用户信息", notes = "id")
    ResponseEntity<ResultBean> getByAttr(@RequestBody RequestBean requestBean) {
        return ResponseHelper.OK(userService.getByAttr(requestBean));
    }
}