package com.coocaa.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.coocaa.common.constant.StringConstant;
import com.coocaa.common.constant.TableConstant;
import com.coocaa.common.request.*;
import com.coocaa.core.log.exception.ApiException;
import com.coocaa.core.log.exception.ApiResultEnum;
import com.coocaa.core.log.response.ResponseHelper;
import com.coocaa.core.log.response.ResultBean;
import com.coocaa.core.mybatis.base.BaseServiceImpl;
import com.coocaa.core.secure.utils.SecureUtil;
import com.coocaa.core.tool.utils.*;
import com.coocaa.prometheus.feign.ITaskClient;
import com.coocaa.user.entity.Team;
import com.coocaa.user.entity.User;
import com.coocaa.user.input.TeamInputVo;
import com.coocaa.user.mapper.TeamMapper;
import com.coocaa.user.mapper.UserMapper;
import com.coocaa.user.output.TeamOutputVo;
import com.coocaa.user.service.TeamService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
    private ITaskClient taskClient;

    @Override
    public ResponseEntity<ResultBean> listByPage(PageRequestBean pageRequestBean) {
        RequestUtil.setDefaultPageBean(pageRequestBean);
        String conditionString = SqlUtil.getConditionString(pageRequestBean.getConditions(), pageRequestBean.getConditionConnection());
        List<Team> list = teamMapper.getPageAll(pageRequestBean.getPage() * pageRequestBean.getCount(), pageRequestBean.getCount(), conditionString, pageRequestBean.getOrderBy(), pageRequestBean.getSortType());
        List<TeamOutputVo> resultList = list.stream().map(this::fillUsers).collect(Collectors.toList());
        Integer pageAllSize = teamMapper.getPageAllSize(conditionString);
        return ResponseHelper.OK(resultList, pageAllSize);
    }

    @Override
    public ResponseEntity<ResultBean> listByPageWithUserCondition(PageRequestBean pageRequestBean) {
        RequestUtil.setDefaultPageBean(pageRequestBean);
        List<PageRequestBean.PageRequestItem> conditions = pageRequestBean.getConditions();
        String conditionConnection = pageRequestBean.getConditionConnection();
        String[] connections = conditionConnection.split(" ");
        List<Set<String>> teamIdsList = new ArrayList<>();
        int i;
        for (i = 0; i < conditions.size(); i++) {
            StringBuffer sql = new StringBuffer();
            SqlUtil.addCondition(sql, conditions.get(i), null);
            List<String> teamIdsStrings = userMapper.getAllTeamIdsString(sql.toString());
            Set<String> teams = new HashSet<>();
            if (CollectionUtil.isEmpty(teamIdsStrings)) {
                teamIdsList.add(teams);
                continue;
            }
            teamIdsStrings.stream().forEach(teamIdsString -> {
                String[] split = teamIdsString.split(StringConstant.COMMA);
                teams.addAll(Arrays.asList(split));
            });
            teamIdsList.add(teams);
        }
        Set<String> allTeamIds = teamMapper.selectAllTeamIds();
        // 每个user的team_ids按与或非求交集并集
        Set<String> realTeamIds = doWithCondition(allTeamIds, teamIdsList, connections);
        if (CollectionUtil.isNotEmpty(realTeamIds)) {
            String oriCondition = StringUtil.addBrackets(String.join(",", realTeamIds));
            String condition = SqlUtil.addLimitCondition(oriCondition, pageRequestBean.getPage(), pageRequestBean.getCount());
            List<TeamOutputVo> resultList = teamMapper.selectByIdInUser(condition).stream().map(this::fillUsers).collect(Collectors.toList());
            Integer pageAllSize = teamMapper.selectByIdInUserSize(oriCondition);
            return ResponseHelper.OK(resultList, pageAllSize);
        }
        return ResponseHelper.OK(Collections.emptyList(), 0);
    }

    private Set<String> doWithCondition(Set<String> allTeamIds, List<Set<String>> teamIdsList, String[] connections) {
        if (teamIdsList.size() == 1) {
            if (StringConstant.NOT.equalsIgnoreCase(connections[0])) {
                allTeamIds.removeAll(teamIdsList.get(0));
                return allTeamIds;
            }
            return teamIdsList.get(0);
        }
        int i;
        Set<String> resultSet = new HashSet<>(allTeamIds);
        Set<String> firstTemp;
        Set<String> secondTemp;
        for (i = 0; i < teamIdsList.size() - 1; i++) {
            switch (connections[i]) {
                case StringConstant.AND:
                    if (i != 0 && StringConstant.NOT.equalsIgnoreCase(connections[i - 1])) {
                        secondTemp = teamIdsList.get(i + 1);
                        resultSet.retainAll(secondTemp);
                    } else {
                        firstTemp = teamIdsList.get(i);
                        secondTemp = teamIdsList.get(i + 1);
                        // 交集
                        firstTemp.retainAll(secondTemp);
                        resultSet.retainAll(firstTemp);
                    }
                    break;
                case StringConstant.OR:
                    if (i != 0 && StringConstant.NOT.equalsIgnoreCase(connections[i - 1])) {
                        secondTemp = teamIdsList.get(i + 1);
                        resultSet.addAll(secondTemp);
                    } else {
                        firstTemp = teamIdsList.get(i);
                        secondTemp = teamIdsList.get(i + 1);
                        // 并集
                        firstTemp.addAll(secondTemp);
                        resultSet.addAll(firstTemp);
                    }
                    break;
                case StringConstant.NOT:
                    Set<String> allTeamIdsTemp = new HashSet<>(allTeamIds);
                    secondTemp = teamIdsList.get(i + 1);
                    allTeamIdsTemp.removeAll(secondTemp);
                    resultSet.retainAll(allTeamIdsTemp);
                    break;
                default:
                    break;
            }
        }
        return resultSet;
    }

    @Override
    @Transactional
    public TeamOutputVo createTeam(TeamInputVo teamInputVo) {
        Long teamInputVoId = teamInputVo.getId();
        // 修改权限判断
        if (teamInputVoId != null && teamInputVoId != 0) {
            Team team = teamMapper.selectById(teamInputVoId);
            Long adminUserId = team.getAdminUserId();
            Long currentUserId = SecureUtil.getUserId();
            if (!currentUserId.equals(adminUserId)) {
                throw new ApiException(ApiResultEnum.USER_NOT_TEAM_ADMIN);
            }
        }
        // 添加判断
        else {
            Integer size = teamMapper.selectCount(new QueryWrapper<Team>().eq(TableConstant.TEAM.NAME, teamInputVo.getName()));
            if (size > 0)
                throw new ApiException(ApiResultEnum.NAME_REPEAT_ERROR);
        }
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
        return fillUsers(team);
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
                    if (replace.length() == 1) {
                        user.setTeamIds("");
                    } else {
                        user.setTeamIds(replace.substring(1, replace.lastIndexOf(",")));
                    }
                    user.updateById();
                });
                taskClient.deleteTeamIdFromTask(team.getId() + "");
                team.deleteById();
            });
        });
    }

    private TeamOutputVo fillUsers(Team team) {
        TeamOutputVo teamOutputVo = BeanUtil.copy(team, TeamOutputVo.class);
        List<User> users = userMapper.selectByTeamIdPage(team.getId(), 0, 10);
        Integer size = userMapper.selectByTeamIdSize(team.getId());
        teamOutputVo.setUserList(users);
        teamOutputVo.setUserListTotal(size);
        List<User> adminUser = users.stream().filter(user -> user.getId().equals(team.getAdminUserId())).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(adminUser))
            teamOutputVo.setAdminUser(adminUser.get(0));
        return teamOutputVo;
    }
}