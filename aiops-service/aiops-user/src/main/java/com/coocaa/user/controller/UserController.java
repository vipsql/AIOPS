package com.coocaa.user.controller;

import com.coocaa.common.request.PageRequestBean;
import com.coocaa.common.request.RequestBean;
import com.coocaa.core.tool.utils.SqlUtil;
import com.coocaa.user.entity.User;
import com.coocaa.user.input.UserInputVo;
import com.coocaa.user.mapper.TeamMapper;
import com.coocaa.user.mapper.UserMapper;
import com.coocaa.user.service.UserService;
import com.coocaa.core.log.response.ResponseHelper;
import com.coocaa.core.log.response.ResultBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @description:
 * @author: dongyang_wu
 * @create: 2019-07-29 14:55
 */
@RestController
@RequestMapping("/users")
@Api(value = "用户模块", tags = "用户接口")
@AllArgsConstructor
public class UserController {
    private UserService userService;
    private UserMapper userMapper;
    private TeamMapper teamMapper;

    @PostMapping("/attr")
    @ApiOperation(value = "获取指定属性的用户信息", notes = "id")
    ResponseEntity<ResultBean> getByAttr(@RequestBody RequestBean requestBean) {
        return ResponseHelper.OK(userService.getByAttr(requestBean));
    }

    @PostMapping
    @ApiOperation(value = "分页获取user列表,响应code为总数")
    ResponseEntity<ResultBean> gets(@RequestBody PageRequestBean pageRequestBean) {
        String conditionString = SqlUtil.getConditionString(pageRequestBean.getConditions(), pageRequestBean.getConditionConnection());
        List<User> list = userMapper.getPageAll(pageRequestBean.getPage() * pageRequestBean.getCount(), pageRequestBean.getCount(), conditionString);
        list.forEach(user -> userService.fillUserTeams(user));
        Integer pageAllSize = userMapper.getPageAllSize(conditionString);
        return ResponseHelper.OK(list, pageAllSize);
    }

    @PostMapping("/create")
    @ApiOperation(value = "添加或修改",
            notes = "通过修改teamIds改变所属的Team  \n")
    ResponseEntity<ResultBean> create(@RequestBody UserInputVo userInputVo) {
        return ResponseHelper.OK(userService.insertOrUpdate(userInputVo));
    }

    @PostMapping("/delete")
    @ApiOperation(value = "批量删除")
    public ResponseEntity<ResultBean> delete(@RequestBody RequestBean requestBean) {
        userService.deletes(requestBean);
        return ResponseHelper.OK();
    }
}