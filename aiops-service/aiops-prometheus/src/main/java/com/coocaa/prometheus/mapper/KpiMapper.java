package com.coocaa.prometheus.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coocaa.common.constant.TableConstant;
import com.coocaa.prometheus.entity.Kpi;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @description: KpiListing数据接口层
 * @author: dongyang_wu
 * @create: 2019-08-08 09:32
 */
public interface KpiMapper extends BaseMapper<Kpi> {
    String GET_PAGE_ALL = TableConstant.GET_PAGE_ALL + TableConstant.TABLE.TABLE_KPI + TableConstant.GET_PAGE_ALL_CONDITION;
    String GET_PAGE_ALL_SIZE = TableConstant.GET_PAGE_ALL_SIZE + TableConstant.TABLE.TABLE_KPI + TableConstant.GET_PAGE_ALL_SIZE_CONDITION;

    // 分页获取数据开始
    @Select(value = GET_PAGE_ALL)
    List<Kpi> getPageAll(Integer page, Integer count, String conditions, String orderBy, String sortType);

    @Select(value = GET_PAGE_ALL_SIZE)
    Integer getPageAllSize(@Param("conditions") String conditions);
    // 分页获取数据结束
}