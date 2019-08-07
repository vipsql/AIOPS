package com.coocaa.prometheus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coocaa.common.constant.TableConstant;
import com.coocaa.prometheus.entity.Metrics;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @description:
 * @author: dongyang_wu
 * @create: 2019-08-05 15:28
 */
public interface MetricsMapper extends BaseMapper<Metrics> {
    String GET_PAGE_ALL = TableConstant.GET_PAGE_ALL + TableConstant.TABLE.TABLE_METRICS + TableConstant.GET_PAGE_ALL_CONDITION;
    String GET_PAGE_ALL_SIZE = TableConstant.GET_PAGE_ALL_SIZE + TableConstant.TABLE.TABLE_METRICS + TableConstant.GET_PAGE_ALL_SIZE_CONDITION;

    // 分页获取数据开始
    @Select(value = GET_PAGE_ALL)
    List<Metrics> getPageAll(Integer page, Integer count, String conditions, String orderBy, String sortType);

    @Select(value = GET_PAGE_ALL_SIZE)
    Integer getPageAllSize(@Param("conditions") String conditions);
    // 分页获取数据结束
}