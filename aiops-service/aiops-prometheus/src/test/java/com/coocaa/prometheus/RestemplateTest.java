package com.coocaa.prometheus;

import com.alibaba.fastjson.*;
import com.coocaa.core.tool.utils.NumberUtil;
import com.coocaa.prometheus.entity.Targets;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @program: intelligent_maintenance
 * @description: Restful测试
 * @author: dongyang_wu
 * @create: 2019-08-02 13:16
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RestemplateTest {
    @Autowired
    RestTemplate restTemplate;

    @Test
    public void testRest() {
        String url = "http://123.207.54.158:9090/api/v1/targets";
        ResponseEntity<String> targetsApiResult = restTemplate.getForEntity(url, String.class);
        JSONArray jsonArray = JSON.parseObject(targetsApiResult.getBody()).getJSONObject("data").getJSONArray("activeTargets");
        System.out.println(jsonArray);
        List<Targets> targets = JSON.parseArray(jsonArray.toJSONString(), Targets.class);
        System.out.println(targets.get(0).getLabels().getInstance());
    }

    @Test
    public void testDouble() {
        String substring = String.format("%.4f", Double.parseDouble("1.66666"));
        System.out.println(Integer.valueOf(substring) * 10000);
    }


}