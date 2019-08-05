package com.coocaa.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coocaa.common.constant.TableConstant;
import com.coocaa.user.entity.Team;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface TeamMapper extends BaseMapper<Team> {

    @Results({
            @Result(column = "id", property = "id", id = true),
//            @Result(column = "admin_user_id", property = "adminUserId"),
//            @Result(column = "admin_user_id", property = "adminUser",
//                    one = @One(select = "com.coocaa.user.mapper.UserMapper.selectById", fetchType = FetchType.EAGER)
//            ),
            @Result(column = "id", property = "userList",
                    many = @Many(
                            select = "com.coocaa.user.mapper.UserMapper.selectByTeamId",
                            fetchType = FetchType.EAGER
                    )
            )
    })
    @Select({TableConstant.TEAM.ATTR, TableConstant.TABLE.TABLE_TEAM, " limit #{page},#{count}"})
    List<Team> getTeams(Integer page, Integer count);
}
