package com.coocaa.prometheus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coocaa.common.constant.TableConstant;
import com.coocaa.prometheus.entity.Task;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author wyx
 * @since 2019-07-25
 */
@Mapper
@Component
public interface TaskMapper extends BaseMapper<Task> {
    String FINDALL = "select * from " + TableConstant.TABLE.TABLE_TASK + " where " + TableConstant.STATUS + " = 1 and " + TableConstant.LOGIC + "= 0";

    boolean updateCronById(@Param("id") Integer id, @Param("cron") String cron);

    IPage<Task> selectAll(Page page);

    @Select(value = FINDALL)
    List<Task> findAll();
}
