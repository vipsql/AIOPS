package com.coocaa.core.tool.utils;


import com.coocaa.core.tool.base.BaseException;
import com.coocaa.core.tool.response.CodeEnum;
import com.coocaa.core.tool.response.ReturnData;

/**
 * @Auther: wyx
 * @Date: 2019-07-25 20:20
 * @Description: 通用方法封装
 */
public class HelpUtil {

    /**
     * 判断对象是否为空
     * @param object
     * @return
     */
    public static ReturnData checkEmpty(Object object){
        if(object == null){
            throw new BaseException(CodeEnum.NO_DATA.getCode(), CodeEnum.NO_DATA.getMsg());
        }
        return ReturnData.success(object);
    }

    /**
     * 判断字符串是否为空
     * @param string
     * @return
     */
    public static boolean isStringEmpty(String string){
        return !(string != null && !string.isEmpty());
    }

    /**
     * 判断数据库操作是否正确
     * @param result
     * @return
     */
    public static ReturnData checkDBResult(boolean result){
        if (result){
            return ReturnData.success();
        }

        throw new BaseException(CodeEnum.DATABASE_OPERATE_FAILURE.getCode(), CodeEnum.DATABASE_OPERATE_FAILURE.getMsg());
    }
}
