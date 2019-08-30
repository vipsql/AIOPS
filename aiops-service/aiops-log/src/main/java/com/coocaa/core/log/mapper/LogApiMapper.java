package com.coocaa.core.log.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coocaa.common.constant.TableConstant;
import com.coocaa.core.log.model.LogApi;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * Mapper 接口
 *
 * @author dongyang_wu
 */
public interface LogApiMapper extends BaseMapper<LogApi> {
    String FIND_TOP_RECORDS = TableConstant.SELECT_ID_ALL + TableConstant.TABLE.LOG_API + " ORDER BY " + TableConstant.CREATE_TIME + " DESC LIMIT ${logNumber}";
    String FIND_ALL_SIZE = TableConstant.SELECT_COUNT + TableConstant.TABLE.LOG_API;

    @Select(value = FIND_TOP_RECORDS)
    List<Long> findTopRecords(@Param("logNumber") String logNumber);

    @Select(value = FIND_ALL_SIZE)
    Long findAllSize();
}
