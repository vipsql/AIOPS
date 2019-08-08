package com.coocaa.prometheus.service;

import com.coocaa.core.mybatis.base.BaseService;
import com.coocaa.prometheus.entity.Kpi;

public interface KpiService extends BaseService<Kpi> {


    Kpi create(Kpi kpi);
}
