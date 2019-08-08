package com.coocaa.prometheus.service.impl;

import com.coocaa.core.mybatis.base.BaseServiceImpl;
import com.coocaa.prometheus.entity.Kpi;
import com.coocaa.prometheus.entity.VectorData;
import com.coocaa.prometheus.mapper.KpiMapper;
import com.coocaa.prometheus.service.KpiService;
import com.coocaa.prometheus.service.PromQLService;
import com.coocaa.prometheus.util.PromQLUtil;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @description: KpiListingService指标清单实现类
 * @author: dongyang_wu
 * @create: 2019-08-08 09:37
 */
@Service
@AllArgsConstructor
public class KpiServiceImpl extends BaseServiceImpl<KpiMapper, Kpi> implements KpiService {
    private PromQLService promQLService;

    @Override
    public Kpi create(Kpi kpi) {
        // 检测指标表达式是否可行
        HashMap<String, String> conditions = new HashMap<>();
        conditions.put("instance", "172.16.33.31:9100");
        String realQuery = PromQLUtil.getQueryConditionStr(kpi.getPromExpression(), conditions);
        List<VectorData> vectorData = promQLService.instantQuery(realQuery, null, null);
        System.out.println(vectorData);
        kpi.insertOrUpdate();
        return kpi;
    }
}