package com.coocaa.common.request;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.coocaa.common.constant.StringConstant;
import com.coocaa.common.constant.TableConstant;

/**
 * @program: intelligent_maintenance
 * @description: 请求工具类
 * @author: dongyang_wu
 * @create: 2019-08-01 12:38
 */
public class RequestUtil {

    public static boolean isInValidParameter(Integer min, Integer max, Integer... list) {
        for (Integer args : list) {
            if (args < min || args > max)
                return true;
        }
        return false;
    }

    public static void setDefaultPageBean(PageRequestBean pageRequestBean) {
        if (pageRequestBean.getPage() == null) {
            pageRequestBean.setPage(0);
        }
        if (pageRequestBean.getCount() == null || pageRequestBean.getCount() == 0) {
            pageRequestBean.setCount(5);
        }
        if (pageRequestBean.getOrderBy() == null) {
            pageRequestBean.setOrderBy(TableConstant.ID);
        }
        if (pageRequestBean.getSortType() == null) {
            pageRequestBean.setSortType(StringConstant.DESC);
        }
    }

    public static void setDefaultPageBean(PageWithTeamRequestBean pageRequestBean) {
        if (pageRequestBean.getPage() == null) {
            pageRequestBean.setPage(0);
        }
        if (pageRequestBean.getCount() == null || pageRequestBean.getCount() == 0) {
            pageRequestBean.setCount(5);
        }
        if (pageRequestBean.getOrderBy() == null) {
            pageRequestBean.setOrderBy(TableConstant.ID);
        }
        if (pageRequestBean.getSortType() == null) {
            pageRequestBean.setSortType(StringConstant.DESC);
        }
    }
}