package com.coocaa.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coocaa.common.constant.TableConstant;
import com.coocaa.user.entity.Team;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Set;

public interface TeamMapper extends BaseMapper<Team> {
    String GET_PAGE_ALL = TableConstant.GET_PAGE_ALL + TableConstant.TABLE.TABLE_TEAM + TableConstant.GET_PAGE_ALL_CONDITION;
    String GET_PAGE_ALL_SIZE = TableConstant.GET_PAGE_ALL_SIZE + TableConstant.TABLE.TABLE_TEAM + TableConstant.GET_PAGE_ALL_SIZE_CONDITION;
    String SELECT_BY_ID_IN_USER = TableConstant.SELECT_ALL + TableConstant.TABLE.TABLE_TEAM + TableConstant.WHERE + TableConstant.ID + " in ${conditions}";
    String SELECT_ADMINUSERIDS_IN_TEAMIDS = "SELECT DISTINCT " + TableConstant.USER.ADMIN_USER_ID + " FROM " + TableConstant.TABLE.TABLE_TEAM + " WHERE ${column} IN ${conditions}";
    String SELECT_BY_ID_IN_USER_SIZE = TableConstant.SELECT_COUNT + TableConstant.TABLE.TABLE_TEAM + TableConstant.WHERE + TableConstant.ID + " in ${conditions}";

    String SELECT_TEAM_IDS = "SELECT ID FROM " + TableConstant.TABLE.TABLE_TEAM + TableConstant.LOGIC_EXIST_STR;

    @Select(value = GET_PAGE_ALL)
    List<Team> getPageAll(Integer page, Integer count, String conditions, String orderBy, String sortType);

    @Select(value = GET_PAGE_ALL_SIZE)
    Integer getPageAllSize(@Param("conditions") String conditions);

    @Select(value = SELECT_BY_ID_IN_USER)
    List<Team> selectByIdInUser(@Param("conditions") String conditions);

    @Select(value = SELECT_BY_ID_IN_USER_SIZE)
    Integer selectByIdInUserSize(@Param("conditions") String conditions);

    @Select(value = SELECT_ADMINUSERIDS_IN_TEAMIDS)
    List<String> selectAdminUserIdsInTeamIds(@Param("column") String column, @Param("conditions") String conditions);

    @Select(value = SELECT_TEAM_IDS)
    Set<String> selectAllTeamIds();

    //    @Results({
//            @Result(column = "id", property = "id", id = true),
//            @Result(column = "admin_user_id", property = "adminUserId"),
//            @Result(column = "admin_user_id", property = "adminUser",
//                    one = @One(select = "com.coocaa.user.mapper.UserMapper.selectById", fetchType = FetchType.EAGER)
//            ),
//            @Result(column = "id", property = "userList",
//                    many = @Many(
//                            select = "com.coocaa.user.mapper.UserMapper.selectByTeamId",
//                            fetchType = FetchType.EAGER
//                    )
//            )
//    })
}
