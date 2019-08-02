package com.monitoring.data_manipulation;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.message.user_message.util.DateUtil;
import com.monitoring.data_manipulation.common.PromNorm;
import com.monitoring.data_manipulation.entity.MatrixData;
import com.monitoring.data_manipulation.entity.Metric;
import com.monitoring.data_manipulation.service.NormSearchService;
import com.monitoring.data_manipulation.service.PromQLService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Auther: wyx
 * @Date: 2019-07-28 12:08
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class PromQLTest {

    @Autowired
    private PromQLService promQLService;

    @Autowired
    private NormSearchService normSearchService;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss.SSS");

    @Test
    public void parseDateTest() throws ParseException {
//        Date date = simpleDateFormat.parse("2019-07-2712:00:00.000");
//        System.out.println(date);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();
        System.out.println(format.format(now));
        System.out.println(DateUtil.getBeforeDate(now, 0, 6, 0, 0));
    }

    @Test
    public void divideTest(){
        BigDecimal total = new BigDecimal("42139451392");
        BigDecimal used = new BigDecimal("10538549248");
        System.out.println(used.divide(total, 4, RoundingMode.CEILING));
    }

    @Test
    public void requestTotalTest() throws ParseException {
        System.out.println("instantQuery");
        System.out.println(promQLService.instantQuery(PromNorm.REQUEST_TOTAL, null, null));

        System.out.println("rangeQuery");
        Date start = simpleDateFormat.parse("2019-07-2712:00:00.000");
        Date end = simpleDateFormat.parse("2019-07-2712:00:30.000");
        System.out.println(promQLService.rangeQuery(PromNorm.REQUEST_TOTAL, start, end, 15));
    }

    @Test
    public void cpuLoadTest() throws ParseException {
        System.out.println("instantQuery");
        System.out.println(promQLService.instantQuery(PromNorm.CPU_LOAD, null, null));

        System.out.println("rangeQuery");
        Date start = simpleDateFormat.parse("2019-07-2712:00:00.000");
        Date end = simpleDateFormat.parse("2019-07-2712:00:30.000");
        System.out.println(promQLService.rangeQuery(PromNorm.CPU_LOAD, start, end, 15));
    }

    @Test
    public void diskCapacityTest() throws ParseException {
//        System.out.println("instantQuery");
//        System.out.println(promQLService.instantQuery(PromNorm.DISK_TOTAL_CAPACITY, null, null));
//        System.out.println(promQLService.instantQuery(PromNorm.DISK_FREE_CAPACITY, null, null));
//
        System.out.println("rangeQuery");
        Date start = simpleDateFormat.parse("2019-07-2712:00:00.000");
        Date end = simpleDateFormat.parse("2019-07-2712:00:30.000");
        System.out.println(normSearchService.queryRangeDisk(start, end, 15));
//        System.out.println(normSearchService.queryInstantDisk(null, null));
    }

    @Test
    public void bandWidthTest() throws ParseException {
        System.out.println("instantQuery");
        System.out.println(promQLService.instantQuery(PromNorm.UPLOAD_BAND_WIDTH, null, null));
        System.out.println(promQLService.instantQuery(PromNorm.DOWNLOAD_BAND_WIDTH, null, null));

        System.out.println("rangeQuery");
        Date start = simpleDateFormat.parse("2019-07-2712:00:00.000");
        Date end = simpleDateFormat.parse("2019-07-2712:00:30.000");
        System.out.println(promQLService.rangeQuery(PromNorm.UPLOAD_BAND_WIDTH, start, end, 15));
        System.out.println(promQLService.rangeQuery(PromNorm.DOWNLOAD_BAND_WIDTH, start, end, 15));
    }

}
