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

    String GET_PAGE_ALL = TableConstant.GET_PAGE_ALL + TableConstant.TABLE.TABLE_TASK + TableConstant.GET_PAGE_ALL_CONDITION;
    String GET_PAGE_ALL_SIZE = TableConstant.GET_PAGE_ALL_SIZE + TableConstant.TABLE.TABLE_TASK + TableConstant.GET_PAGE_ALL_SIZE_CONDITION;

    String SELECT_TASK_BY_TEAMIDS = TableConstant.SELECT_ALL + TableConstant.TABLE.TABLE_TASK + TableConstant.LOGIC_EXIST_STR + "and FIND_IN_SET(${teamIds}," + TableConstant.USER.TEAM_IDS + ")";

    String GET_CAN_RUN_TASK = "SELECT * FROM task WHERE logic=0  AND team_ids IS NOT NULL AND team_ids <> ''  AND STATUS = 1 AND error_number <= 100  AND (instance IS NULL OR instance=''  OR instance = #{currentInstance}) LIMIT ${page},${count}";

    // 分页获取数据开始
    @Select(value = GET_PAGE_ALL)
    List<Task> getPageAll(Integer page, Integer count, String conditions, String orderBy, String sortType);

    @Select(value = GET_PAGE_ALL_SIZE)
    Integer getPageAllSize(@Param("conditions") String conditions);

    // 分页获取数据结束
    boolean updateCronById(@Param("id") Integer id, @Param("cron") String cron);

    IPage<Task> selectAll(Page page);

    @Select(value = FINDALL)
    List<Task> findAll();

    @Select(value = SELECT_TASK_BY_TEAMIDS)
    List<Task> selectTaskByTeamIds(@Param("teamIds") String teamIds);

    @Select(value = GET_CAN_RUN_TASK)
    List<Task> getCanRunTask(@Param("page") Integer page, @Param("count") Integer count, @Param("currentInstance") String currentInstance);
}
