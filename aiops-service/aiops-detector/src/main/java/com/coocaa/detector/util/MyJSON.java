package com.coocaa.detector.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author 陈煜坤
 * @date 2019/8/2  16:16
 * @package_name com.monitoring.warn.notice.utils
 * @version 1.0
 */
public class MyJSON {


    /**
     * 本json转换工具目前支持json转换为所添加的类，只能添加属性类型为 int String
     * 1. 适用于json格式有多层，可自己的类只有1层的情况
     * 2. 类需提供set方法，方法需要为public的，且json字段里的属性名要与类的一致（目前是这样的），有且只有一个，不可以迭代多个名字相同的属性
     * 3. 继续完善中
     * @param json json数据
     * @param clazz 所需要转换的类
     * @param <T> 泛型
     * @return
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     */
    public static <T> T jsonToObject(String json,Class<T> clazz) throws NoSuchMethodException, IllegalAccessException, InstantiationException, InvocationTargetException {
        T t = clazz.newInstance();
        Field[] declaredFields = clazz.getDeclaredFields();
        for(Field field : declaredFields){
            Method method = clazz.getMethod("set"+ toFirstUpperCase(field.getName()),field.getType());
            if (field.getType() == String.class){
                String[] split = json.split("\"" + field.getName() + "\": \"");
                String string = null;
                if (split.length>1){
                    string = split[1].substring(0,split[1].indexOf("\""));
                }
                method.invoke(t,string);
            }else if (field.getType() == int.class){
                String[] split = json.split("\"" + field.getName() + "\": ");
                int number = 0;
                if (split.length>1){
                    int i = split[1].indexOf(",");
                    if (i == -1){
                        i = split[1].indexOf("}");
                    }
                    number = Integer.valueOf(split[1].substring(0,i));
                }
                method.invoke(t,number);
            }
        }
        return t;
    }
    public static String toFirstUpperCase(String str){
        String t = str.substring(0, 1).toUpperCase() + str.substring(1);
        char[] chars = str.toCharArray();
        if (chars[0] >= 'a' && chars[0] <= 'z') {
            chars[0] = (char)(chars[0] - 32);
        }
        return new String(chars);
    }
}
