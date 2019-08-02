package com.coocaa.common.util;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Auther: wyx
 * @Date: 2019-07-25 21:46
 * @Description: 日期工具类
 */
public class DateUtil {

    /**
     * 返回一定时间后的日期
     * @param date 开始计时的时间
     * @param year 增加的年
     * @param month 增加的月
     * @param day 增加的日
     * @param hour 增加的小时
     * @param minute 增加的分钟
     * @param second 增加的秒
     * @return
     */
    public static Date getAfterDate(Date date, int year, int month, int day, int hour, int minute, int second){
        if(date == null){
            date = new Date();
        }

        Calendar cal = new GregorianCalendar();

        cal.setTime(date);
        if(year != 0){
            cal.add(Calendar.YEAR, year);
        }
        if(month != 0){
            cal.add(Calendar.MONTH, month);
        }
        if(day != 0){
            cal.add(Calendar.DATE, day);
        }
        if(hour != 0){
            cal.add(Calendar.HOUR_OF_DAY, hour);
        }
        if(minute != 0){
            cal.add(Calendar.MINUTE, minute);
        }
        if(second != 0){
            cal.add(Calendar.SECOND, second);
        }
        return cal.getTime();
    }

    public static Date getBeforeDate(Date date, int day, int hour, int minute, int second){
        if(date == null){
            date = new Date();
        }

        long time = 0L;
        long secondTime = 1000 * second;
        long minuteTime = 1000 * 60 * minute;
        long hourTime = 1000 * 60 * 60 * hour;
        long dayTime = 1000 * 60 * 60 * 24 * day;
        time = date.getTime() - secondTime - minuteTime - hourTime - dayTime;

        return new Date(time);
    }

    /**
     * 返回当天的时间范围
     * eg: 2019-5-10 19:39:51
     * 2019-05-10 00:00:00 ~ 2019-05-10 23:59:59
     */
    public static Map<String, String> getDayRange(Date date){
        Map<String, String> map = new HashMap<>();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String nowDate = simpleDateFormat.format(date);
        String leftTime = nowDate + " 00:00:00";
        String rightTime = nowDate + " 23:59:59";

        map.put("leftTime", leftTime);
        map.put("rightTime", rightTime);
        return map;
    }

}