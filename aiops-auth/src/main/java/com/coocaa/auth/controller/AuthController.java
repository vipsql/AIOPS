package com.coocaa.auth.controller;

import com.coocaa.auth.service.AuthService;
import com.coocaa.common.constant.Constant;
import com.coocaa.core.log.exception.ApiException;
import com.coocaa.core.log.exception.ApiResultEnum;
import com.coocaa.core.log.response.ResponseHelper;
import com.coocaa.core.log.response.ResultBean;
import com.coocaa.core.tool.utils.Func;
import com.coocaa.user.input.LoginUserVo;
import io.swagger.annotations.*;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @program: intelligent_maintenance
 * @description: 授权控制器
 * @author: dongyang_wu
 * @create: 2019-07-28 22:09
 */
@RestController
@Api(value = "用户授权认证", tags = "授权接口")
@AllArgsConstructor
public class AuthController {
    private AuthService authService;

    @PostMapping("/auth/token")
    @ApiOperation(value = "获取认证token", notes = "账号:account,密码:password,type:0内部账号登录1酷开域账号登录")
    public ResponseEntity<ResultBean> token(@Validated @RequestBody LoginUserVo loginUserVo, @RequestParam Integer type, @ApiIgnore Errors errors) {
        if (Func.isEmpty(type)) {
            throw new ApiException(ApiResultEnum.QUERY_ARGS_ERROR);
        }
        if (Constant.NumberType.ZERO_PROPERTY.equals(type)) {
            return authService.token(loginUserVo.getUserName(), loginUserVo.getPassword());
        } else if (Constant.NumberType.ONE_PROPERTY.equals(type)) {
            return authService.tokenByLdap(loginUserVo.getUserName(), loginUserVo.getPassword());
        }
        throw new ApiException(ApiResultEnum.FUNCTION_NOT_EXEC_ERROR);
    }

}