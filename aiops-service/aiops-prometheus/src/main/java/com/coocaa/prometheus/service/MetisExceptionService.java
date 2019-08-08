package com.coocaa.prometheus.service;

import com.coocaa.common.request.PageRequestBean;
import com.coocaa.core.log.response.ResultBean;
import com.coocaa.core.mybatis.base.BaseService;
import com.coocaa.prometheus.entity.MetisException;
import com.coocaa.prometheus.input.MetisExceptionInputVo;
import org.springframework.http.ResponseEntity;

/**
 * @description: MetisException服务类
 * @author: dongyang_wu
 * @create: 2019-08-07 10:02
 */
public interface MetisExceptionService extends BaseService<MetisException> {


    void update(MetisExceptionInputVo metisExceptionInputVo);

    ResponseEntity<ResultBean> listByPage(PageRequestBean pageRequestBean);
}