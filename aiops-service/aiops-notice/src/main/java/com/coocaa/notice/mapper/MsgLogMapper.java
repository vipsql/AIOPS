package com.coocaa.notice.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coocaa.notice.entity.MsgLog;

import java.util.List;

public interface MsgLogMapper extends BaseMapper<MsgLog> {

    void updateStatus(MsgLog msgLog);

    List<MsgLog> selectTimeoutMsg();

    void updateTryCount(MsgLog msgLog);

    MsgLog selectByPrimaryKey(Long id);

}
