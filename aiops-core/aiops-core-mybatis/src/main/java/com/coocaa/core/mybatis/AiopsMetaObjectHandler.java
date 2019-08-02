package com.coocaa.core.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * mybatisplus自定义填充
 *
 * @author dongyang_wu
 */
@Slf4j
@Component
public class AiopsMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        // 获取到需要被填充的字段的值
//        Object fieldValue = getFieldValByName("createTime", metaObject);
        System.err.println("**********插入操作 满足填充条件**********");
        this.setFieldValByName("createTime", new Date(), metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        // 获取到需要被填充的字段的值
//        Object fieldValue = getFieldValByName("updateTime", metaObject);
        System.err.println("**********更新操作 满足填充条件**********");
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }

}
