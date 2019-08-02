package com.coocaa.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coocaa.user.entity.Machine;
import com.coocaa.user.entity.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

/**
 * @program: intelligent_maintenance
 * @description:
 * @author: dongyang_wu
 * @create: 2019-07-29 14:52
 */
public interface MachineMapper extends BaseMapper<Machine> {
    @Select("select * from machine where user_id = #{userId}")
    List<Machine> selectByUserId(Long userId);

}