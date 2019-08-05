package com.coocaa.user.service;

import com.coocaa.core.mybatis.base.BaseService;
import com.coocaa.user.entity.Team;
import com.coocaa.user.input.TeamInputVo;


public interface TeamService extends BaseService<Team> {

    Team createTeam(TeamInputVo teamInputVo);
}
