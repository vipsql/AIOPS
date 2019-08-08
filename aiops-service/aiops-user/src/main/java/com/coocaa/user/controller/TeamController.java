package com.coocaa.user.controller;

import com.coocaa.common.request.*;
import com.coocaa.core.log.response.ResponseHelper;
import com.coocaa.core.log.response.ResultBean;
import com.coocaa.user.input.TeamInputVo;
import com.coocaa.user.service.TeamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @description: Team控制层
 * @author: dongyang_wu
 * @create: 2019-08-05 16:19
 */
@RestController
@RequestMapping("/teams")
@Api(value = "Team模块", tags = "Team接口")
@AllArgsConstructor
public class TeamController {
    private TeamService teamService;

    @PostMapping
    @ApiOperation(value = "分页获取team列表,响应code为总数",
            notes = "conditionConnection可取and、or  \n" +
                    "connection可取=、like、大于、小于  \n" +
                    "时间格式需为2019-08-06 09:44:04  \n" +
                    "page,count必须传 \n" +
                    "query,connection,queryString三者用于模糊查询 \n" +
                    "例： \n" +
                    "{\n" +
                    "  \"page\": 0,\n" +
                    "  \"count\": 5,\n" +
                    "  \"conditionConnection\":\"or\",\n" +
                    "  \"conditions\": [\n" +
                    "    {\n" +
                    "      \"query\": \"update_time\",\n" +
                    "      \"connection\": \"小于\",\n" +
                    "      \"queryString\": \"2019-08-06 09:44:04\"\n" +
                    "    },\n" +
                    " {\n" +
                    "      \"query\": \"admin_user_id\",\n" +
                    "      \"connection\": \"=\",\n" +
                    "      \"queryString\": \"1\"\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}  \n")
    public ResponseEntity<ResultBean> gets(@RequestBody PageRequestBean pageRequestBean) {
        return teamService.listByPage(pageRequestBean);
    }

    @PostMapping("/create")
    @ApiOperation(value = "新建或修改",
            notes = "例:{\n" +
                    "  \"id\":11,\n" +
                    "  \"name\": \"测试Team3\",\n" +
                    "  \"adminUserId\": 1,\n" +
                    "  \"userIdList\": \"21,22\"\n" +
                    "}  \n" +
                    "userIdList以,隔开  \n" +
                    "可以通过改变userIdList改变小组成员")
    public ResponseEntity<ResultBean> create(@RequestBody TeamInputVo teamInputVo) {
        return ResponseHelper.OK(teamService.createTeam(teamInputVo));
    }

    @PostMapping("/delete")
    @ApiOperation(value = "批量删除")
    public ResponseEntity<ResultBean> delete(@RequestBody RequestBean requestBean) {
        teamService.deletes(requestBean);
        return ResponseHelper.OK();
    }

    @PostMapping("/users/{connection}")
    @ApiOperation(value = "获取某个Team下的所有用户",
            notes = "connection: and或or" +
                    "例:  \n" +
                    "[ \"13\",\"14\" ]")
    public ResponseEntity<ResultBean> getTeamUsers(@RequestBody List<String> teamIds, @PathVariable String connection) {
        return ResponseHelper.OK(teamService.getTeamUsers(teamIds, connection));
    }
}