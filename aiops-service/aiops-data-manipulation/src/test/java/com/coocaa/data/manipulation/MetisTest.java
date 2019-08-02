package com.monitoring.data_manipulation;

import com.alibaba.fastjson.JSON;
import com.message.user_message.util.DateUtil;
import com.monitoring.data_manipulation.entity.PromMatrixData;
import com.monitoring.data_manipulation.service.NormSearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.Date;

/**
 * @Auther: wyx
 * @Date: 2019-07-29 17:41
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MetisTest {

    @Autowired
    private NormSearchService normSearchService;

    @Autowired
    private RestTemplate restTemplate;

    class Data{
        int ret;
        String p;
    }

    class Res{
        int code;
        String msg;
        Data data;
    }

    @Test
    public void predictTest(){
    }

}
