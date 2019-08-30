package com.coocaa.user.service;

import com.coocaa.common.request.PageRequestBean;
import com.coocaa.core.log.response.ResultBean;
import com.coocaa.core.mybatis.base.BaseService;
import com.coocaa.user.entity.Team;
import com.coocaa.user.entity.User;
import com.coocaa.user.input.TeamInputVo;
import com.coocaa.user.output.TeamOutputVo;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Set;


public interface TeamService extends BaseService<Team> {

    ResponseEntity<ResultBean> listByPage(PageRequestBean pageRequestBean);

    ResponseEntity<ResultBean> listByPageWithUserCondition(PageRequestBean pageRequestBean);

    TeamOutputVo createTeam(TeamInputVo teamInputVo);

    Set<User> getTeamUsers(List<String> teamIds, String connection);
}
