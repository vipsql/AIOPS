package com.coocaa.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coocaa.common.constant.TableConstant;
import com.coocaa.user.entity.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

/**
 * @description: User数据接口层
 * @author: dongyang_wu
 * @create: 2019-07-29 14:52
 */
public interface UserMapper extends BaseMapper<User> {
    String SELECT_BY_TEAMID_ALL = TableConstant.SELECT_ALL + TableConstant.TABLE.TABLE_USER + TableConstant.LOGIC_EXIST_STR + " AND FIND_IN_SET(#{teamId}," + TableConstant.USER.TEAM_IDS + ")";
    String SELECT_BY_TEAMID_ALL_SIZE = TableConstant.SELECT_COUNT + TableConstant.TABLE.TABLE_USER + TableConstant.LOGIC_EXIST_STR + " AND FIND_IN_SET(#{teamId}," + TableConstant.USER.TEAM_IDS + ")";
    String SELECT_BY_TEAMID_PAGE = TableConstant.SELECT_ALL + TableConstant.TABLE.TABLE_USER + TableConstant.LOGIC_EXIST_STR + " AND FIND_IN_SET(#{teamId}," + TableConstant.USER.TEAM_IDS + ") LIMIT #{page},#{count}";
    String GET_PAGE_ALL = TableConstant.GET_PAGE_ALL + TableConstant.TABLE.TABLE_USER + TableConstant.GET_PAGE_ALL_CONDITION;
    String GET_PAGE_ALL_SIZE = TableConstant.GET_PAGE_ALL_SIZE + TableConstant.TABLE.TABLE_USER + TableConstant.GET_PAGE_ALL_SIZE_CONDITION;

    @Select("SELECT * FROM `user` WHERE id = #{id}")
    User getUser(Long id);

    // 获取响应Team下的所有用户开始
    @Select(value = SELECT_BY_TEAMID_ALL)
    List<User> selectByTeamId(Long teamId);

    @Select(value = SELECT_BY_TEAMID_PAGE)
    List<User> selectByTeamIdPage(Long teamId, Integer page, Integer count);

    @Select(value = SELECT_BY_TEAMID_ALL_SIZE)
    Integer selectByTeamIdSize(Long teamId);
    // 获取响应Team下的所有用户结束

    // 分页获取数据开始
    @Select(value = GET_PAGE_ALL)
    List<User> getPageAll(Integer page, Integer count, String conditions, String orderBy, String sortType);

    @Select(value = GET_PAGE_ALL_SIZE)
    Integer getPageAllSize(@Param("conditions") String conditions);
    // 分页获取数据结束
}