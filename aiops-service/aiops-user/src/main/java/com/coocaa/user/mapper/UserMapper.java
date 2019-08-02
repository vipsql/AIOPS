package com.coocaa.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coocaa.user.entity.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

/**
 * @program: intelligent_maintenance
 * @description:
 * @author: dongyang_wu
 * @create: 2019-07-29 14:52
 */
public interface UserMapper extends BaseMapper<User> {
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
}