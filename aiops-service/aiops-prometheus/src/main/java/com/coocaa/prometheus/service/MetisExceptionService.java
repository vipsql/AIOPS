package com.coocaa.prometheus.service;

import com.coocaa.core.mybatis.base.BaseService;
import com.coocaa.prometheus.entity.MetisException;
import com.coocaa.prometheus.input.MetisExceptionInputVo;

/**
 * @description: MetisException服务类
 * @author: dongyang_wu
 * @create: 2019-08-07 10:02
 */
public interface MetisExceptionService extends BaseService<MetisException> {


    void update(MetisExceptionInputVo metisExceptionInputVo);
}