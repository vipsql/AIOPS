package com.coocaa.common.request;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.coocaa.common.constant.StringConstant;
import com.coocaa.common.constant.TableConstant;

import java.util.Optional;

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
        pageRequestBean.setPage(Optional.ofNullable(pageRequestBean.getPage()).orElse(0));
        pageRequestBean.setCount(Optional.ofNullable(pageRequestBean.getCount()).orElse(5));
        pageRequestBean.setOrderBy(Optional.ofNullable(pageRequestBean.getOrderBy()).orElse(TableConstant.ID));
        pageRequestBean.setSortType(Optional.ofNullable(pageRequestBean.getSortType()).orElse(StringConstant.DESC));
        if (!CollectionUtils.isEmpty(pageRequestBean.getConditions()) && StringUtils.isEmpty(pageRequestBean.getConditionConnection()))
            pageRequestBean.setConditionConnection(StringConstant.AND);
    }
}