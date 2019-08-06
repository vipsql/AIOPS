package com.coocaa.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.coocaa.common.constant.StringConstant;
import com.coocaa.common.constant.TableConstant;
import com.coocaa.common.request.RequestBean;
import com.coocaa.core.mybatis.base.BaseServiceImpl;
import com.coocaa.core.tool.utils.*;
import com.coocaa.user.entity.Team;
import com.coocaa.user.entity.User;
import com.coocaa.user.input.TeamInputVo;
import com.coocaa.user.mapper.TeamMapper;
import com.coocaa.user.mapper.UserMapper;
import com.coocaa.user.service.TeamService;
import lombok.AllArgsConstructor;
import org.bouncycastle.jcajce.provider.symmetric.TEA;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


/**
 * @description: Team管理Service层
 * @author: dongyang_wu
 * @create: 2019-08-05 16:17
 */
@Service
@AllArgsConstructor
public class TeamServiceImpl extends BaseServiceImpl<TeamMapper, Team> implements TeamService {
    private UserMapper userMapper;
    private TeamMapper teamMapper;

    @Override
    @Transactional
    public Team createTeam(TeamInputVo teamInputVo) {
        Team team = new Team();
        BeanUtils.copyProperties(teamInputVo, team);
        String userIdList = StringUtil.isEmpty(teamInputVo.getUserIdList()) ?
                StringUtil.getCommaStr(team.getAdminUserId()) :
                StringUtil.getCommaStr(teamInputVo.getUserIdList()) + team.getAdminUserId() + ",";
        // 更新操作-----user表中的team_id
        boolean idFlag = ObjectUtil.isNotEmpty(team.getId()) && team.getId() > 0;
        if (idFlag) {
            List<User> users = userMapper.selectByTeamId(team.getId());
            users.forEach(user -> {
                if (StringUtil.isEmpty(user.getTeamIds()))
                    return;
                // 遍历原来team的成员去除不在上述userIds列表的中的用户team_ids
                String key = StringUtil.getCommaStr(user.getId());
                if (!userIdList.contains(key)) {
                    String replace = StringUtil.getCommaStr(user.getTeamIds()).replace(StringUtil.getCommaStr(team.getId()), ",");
                    user.setTeamIds(replace.substring(1, replace.lastIndexOf(",")));
                    user.updateById();
                }
            });
        }
        boolean flag = team.insertOrUpdate();
        // 新增操作-----前提用户已经存在
        if (flag) {
            // 在上述userIds列表的中的用户team_ids添加对应team_id
            String[] userIds = userIdList.split(",");
            List<User> users = userMapper.selectList(new QueryWrapper<User>().in(TableConstant.ID, userIds));
            users.forEach(user -> {
                if (StringUtil.isEmpty(user.getTeamIds()))
                    return;
                // 若现在team成员中不含此teamId
                if (!(StringUtil.getCommaStr(user.getTeamIds()).contains(StringUtil.getCommaStr(team.getId()))))
                    user.setTeamIds(user.getTeamIds() + "," + team.getId());
                user.updateById();
            });
        }
        List<User> users = userMapper.selectByTeamId(team.getId());
        team.setUserList(users);
        return team;
    }

    @Override
    public Set<User> getTeamUsers(List<String> teamIds, String connection) {
        Set<User> resultUserSet = new HashSet<>();
        boolean isAndConnection = StringConstant.AND.equalsIgnoreCase(connection);
        teamIds.forEach(teamId -> {
            List<User> users = userMapper.selectByTeamId(Long.valueOf(teamId));
            if (isAndConnection) {
                users.forEach(user -> {
                    List<String> split = Arrays.asList(user.getTeamIds().split(","));
                    boolean containsAllFlag = split.containsAll(teamIds);
                    if (containsAllFlag)
                        resultUserSet.add(user);
                });
            } else {
                resultUserSet.addAll(users);
            }
        });
        return resultUserSet;
    }

    @Override
    @Transactional
    public void deletes(RequestBean requestBean) {
        requestBean.getItems().forEach(item -> {
            List<Team> teams = teamMapper.selectByMap(SqlUtil.map(item.getQuery(), item.getQueryString()).build());
            teams.forEach(team -> {
                List<User> users = userMapper.selectByTeamId(team.getId());
                users.forEach(user -> {
                    if (StringUtil.isEmpty(user.getTeamIds()))
                        return;
                    // 遍历user删除指定的team_id
                    String replace = StringUtil.getCommaStr(user.getTeamIds()).replace(StringUtil.getCommaStr(team.getId()), ",");
                    user.setTeamIds(replace.substring(1, replace.lastIndexOf(",")));
                    user.updateById();
                });
                team.deleteById();
            });
        });
    }
}