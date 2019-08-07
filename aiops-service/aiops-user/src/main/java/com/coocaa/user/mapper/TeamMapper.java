package com.coocaa.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coocaa.common.constant.TableConstant;
import com.coocaa.user.entity.Team;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface TeamMapper extends BaseMapper<Team> {
    String GET_PAGE_ALL = TableConstant.GET_PAGE_ALL + TableConstant.TABLE.TABLE_TEAM + TableConstant.GET_PAGE_ALL_CONDITION;
    String GET_PAGE_ALL_SIZE = TableConstant.GET_PAGE_ALL_SIZE + TableConstant.TABLE.TABLE_TEAM + TableConstant.GET_PAGE_ALL_SIZE_CONDITION;
    String SELECT_BY_ID_IN_USER = TableConstant.SELECT_ALL + TableConstant.TABLE.TABLE_TEAM + TableConstant.WHERE + TableConstant.ID + " in ${conditions}";

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
    @Select(value = GET_PAGE_ALL)
    List<Team> getPageAll(Integer page, Integer count, String conditions, String orderBy, String sortType);

    @Select(value = GET_PAGE_ALL_SIZE)
    Integer getPageAllSize(@Param("conditions") String conditions);

    @Select(value = SELECT_BY_ID_IN_USER)
    List<Team> selectByIdInUser(@Param("conditions") String conditions);
}
