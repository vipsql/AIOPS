package com.coocaa.prometheus;

import com.coocaa.core.log.response.ResultBean;
import com.coocaa.prometheus.common.PromNorm;
import com.coocaa.prometheus.service.PromQLService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;


/**
 * @program: intelligent-maintenance
 * @description: API测试
 * @author: dongyang_wu
 * @create: 2019-08-05 09:31
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PromtheusApiTest {
    @Autowired
    private PromQLService promQLService;

    @Test
    public void testRest() {
//        ResponseEntity<ResultBean> conditionByMetricsName = promQLService.getConditionByMetricsName(PromNorm.REQUEST_TOTAL.replace("%s", ""));
//        System.out.println(conditionByMetricsName.getBody().getData());
    }
}