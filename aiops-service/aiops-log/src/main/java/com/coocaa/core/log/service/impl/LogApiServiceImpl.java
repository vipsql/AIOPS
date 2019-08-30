package com.coocaa.core.log.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coocaa.common.constant.TableConstant;
import com.coocaa.core.log.mapper.LogApiMapper;
import com.coocaa.core.log.model.LogApi;
import com.coocaa.core.log.service.ILogApiService;
import com.coocaa.core.tool.utils.Func;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 服务实现类
 *
 * @author dongyang_wu
 */
@Service
@AllArgsConstructor
public class LogApiServiceImpl extends ServiceImpl<LogApiMapper, LogApi> implements ILogApiService {
    private LogApiMapper logApiMapper;

    @Override
    public void deleteLogs() {
        if (logApiMapper.findAllSize() > 0) {
            List<Long> ids = logApiMapper.findTopRecords(Func.toStr(50));
            this.remove(new QueryWrapper<LogApi>().notIn(TableConstant.ID, ids));
        }
    }
}
