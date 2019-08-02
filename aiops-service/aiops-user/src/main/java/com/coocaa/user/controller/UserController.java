package com.coocaa.user.controller;

import com.coocaa.user.service.UserService;
import com.coocaa.core.log.response.ResponseHelper;
import com.coocaa.core.log.response.ResultBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @program: intelligent_maintenance
 * @description:
 * @author: dongyang_wu
 * @create: 2019-07-29 14:55
 */
@RestController
@Api(value = "用户模块", tags = "用户接口")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @GetMapping("/{id}")
    @ApiOperation(value = "获取指定id用户", notes = "id")
    ResponseEntity<ResultBean> get(@PathVariable Long id) {
        return ResponseHelper.OK(userService.getBaseMapper().selectById(id));
    }
}