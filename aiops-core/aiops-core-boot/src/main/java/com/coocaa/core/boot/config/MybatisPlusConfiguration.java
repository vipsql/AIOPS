package com.coocaa.core.boot.config;

import com.baomidou.mybatisplus.extension.injector.LogicSqlInjector;
import com.baomidou.mybatisplus.extension.plugins.*;
import com.coocaa.core.secure.constant.AppConstant;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.*;

/**
 * mybatisplus 配置
 *
 * @author dongyang_wu
 */
@Configuration
@MapperScan("com.coocaa.**.mapper.**")
public class MybatisPlusConfiguration {

    @Bean
    @ConditionalOnMissingBean(PaginationInterceptor.class)
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }

    @Bean
    public LogicSqlInjector logicSqlInjector() {
        return new LogicSqlInjector();
    }

    /**
     * SQL执行效率插件
     *
     * @return PerformanceInterceptor
     */
    @Bean
    @Profile({AppConstant.DEV_CODE, AppConstant.TEST_CODE})
    public PerformanceInterceptor performanceInterceptor() {
        return new PerformanceInterceptor();
    }

    /**
     * 乐观锁插件
     * @return
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLockerInterceptor() {
        return new OptimisticLockerInterceptor();
    }
}

