package com.coocaa.prometheus.util;

import com.coocaa.core.tool.utils.SpringUtil;
import com.coocaa.prometheus.service.KpiService;
import com.coocaa.prometheus.service.PromQLService;
import com.coocaa.prometheus.service.impl.AsyncServiceTask;

/**
 * @description: 枚举单例
 * @author: dongyang_wu
 * @create: 2019-08-01 14:12
 */
public enum SingletonEnum {
    INSTANCE;
    private KpiService kpiService;
    private AsyncServiceTask asyncServiceTask;
    private PromQLService promQLService;

    // 同时每个枚举实例都是static final类型的，也就表明只能被实例化一次
    SingletonEnum() {
        kpiService = SpringUtil.getBean(KpiService.class);
        asyncServiceTask = SpringUtil.getBean(AsyncServiceTask.class);
        promQLService = SpringUtil.getBean(PromQLService.class);
    }

    public KpiService getKpiService() {
        return kpiService;
    }

    public PromQLService getPromQLService() {
        return promQLService;
    }

    public AsyncServiceTask getAsyncServiceTask() {
        return asyncServiceTask;
    }
}
