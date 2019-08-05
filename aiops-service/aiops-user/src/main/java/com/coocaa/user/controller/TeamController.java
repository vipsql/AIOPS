package com.coocaa.user.controller;

import com.coocaa.core.log.response.ResponseHelper;
import com.coocaa.core.log.response.ResultBean;
import com.coocaa.user.input.TeamInputVo;
import com.coocaa.user.mapper.TeamMapper;
import com.coocaa.user.service.TeamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @description: Team控制层
 * @author: dongyang_wu
 * @create: 2019-08-05 16:19
 */
@RestController
@Api(value = "Team模块", tags = "Team接口")
@AllArgsConstructor
public class TeamController {
    private TeamService teamService;
    private TeamMapper teamMapper;

    @PostMapping("/team")
    @ApiOperation(value = "新建或修改team",
            notes = "例:{\n" +
                    "  \"id\":11,\n" +
                    "  \"name\": \"测试Team3\",\n" +
                    "  \"adminUserId\": 1,\n" +
                    "  \"userIdList\": \"21,22\"\n" +
                    "}  \n" +
                    "userIdList以,隔开  \n" +
                    "可以通过改变userIdList改变小组成员")
    ResponseEntity<ResultBean> create(@RequestBody TeamInputVo teamInputVo) {
        return ResponseHelper.OK(teamService.createTeam(teamInputVo));
    }

    @GetMapping("/teams/{page}/{count}")
    @ApiOperation(value = "分页获取team列表")
    ResponseEntity<ResultBean> gets(@PathVariable Integer page, @PathVariable Integer count) {
        return ResponseHelper.OK(teamMapper.getTeams(page, count));
    }

}