package com.monitoring.data_manipulation.config;

import com.baomidou.mybatisplus.core.parser.ISqlParser;
import com.baomidou.mybatisplus.extension.parsers.BlockAttackSqlParser;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.ArrayList;
import java.util.List;

/**
 * @Auther: wyx
 * @Date: 2019-07-25 15:57
 * @Description: MyBatis-plus 配置类
 */
@EnableTransactionManagement
@Configuration
public class MyBatisPlusConfig {

    /**
     * 分页插件
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();

        /**
         * 防止恶意全表更新删除
         */
        List<ISqlParser> sqlParsers = new ArrayList<>();
        sqlParsers.add(new BlockAttackSqlParser());

        return paginationInterceptor;
    }

}
