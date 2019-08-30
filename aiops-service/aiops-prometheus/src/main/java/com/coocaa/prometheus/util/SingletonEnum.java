package com.coocaa.prometheus.util;

import com.coocaa.core.tool.utils.SpringUtil;
import com.coocaa.prometheus.service.*;
import com.coocaa.prometheus.service.impl.AsyncServiceTask;
import lombok.Getter;

/**
 * @description: 枚举单例
 * @author: dongyang_wu
 * @create: 2019-08-01 14:12
 */
public enum SingletonEnum {
    INSTANCE;
    @Getter
    private KpiService kpiService;
    @Getter
    private AsyncServiceTask asyncServiceTask;
    @Getter
    private PromQLService promQLService;
    @Getter
    private TaskService taskService;
    @Getter
    private TaskManager taskManager;

    // 同时每个枚举实例都是static final类型的，也就表明只能被实例化一次
    SingletonEnum() {
        kpiService = SpringUtil.getBean(KpiService.class);
        asyncServiceTask = SpringUtil.getBean(AsyncServiceTask.class);
        promQLService = SpringUtil.getBean(PromQLService.class);
        taskService = SpringUtil.getBean(TaskService.class);
        taskManager = SpringUtil.getBean(TaskManager.class);
    }
}
