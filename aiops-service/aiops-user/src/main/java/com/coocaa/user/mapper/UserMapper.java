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
    String SELECT_BY_TEAMID = "SELECT * FROM " + TableConstant.TABLE.TABLE_USER + " WHERE FIND_IN_SET(#{teamId}," + TableConstant.USER.TEAM_IDS + ")";

    @Results({
            @Result(column = "id", property = "machines",
                    many = @Many(
                            select = "com.coocaa.user.mapper.MachineMapper.selectByUserId",
                            fetchType = FetchType.EAGER
                    )
            )
    })
    @Select("SELECT * FROM `user` WHERE id = #{id}")
    User getUser(Long id);

    @Select(value = SELECT_BY_TEAMID)
    List<User> selectByTeamId(Long teamId);

}