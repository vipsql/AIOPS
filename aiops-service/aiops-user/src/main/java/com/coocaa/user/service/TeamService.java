package com.coocaa.user.service;

import com.coocaa.core.mybatis.base.BaseService;
import com.coocaa.user.entity.Team;
import com.coocaa.user.entity.User;
import com.coocaa.user.input.TeamInputVo;

import java.util.List;
import java.util.Set;


public interface TeamService extends BaseService<Team> {

    Team createTeam(TeamInputVo teamInputVo);

    Set<User> getTeamUsers(List<String> teamIds, String connection);
}
