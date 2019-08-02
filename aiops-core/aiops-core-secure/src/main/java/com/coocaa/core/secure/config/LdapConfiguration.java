package com.coocaa.core.secure.config;

import com.coocaa.core.secure.utils.LdapUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @program: intelligent_maintenance
 * @description: 域账号登录
 * @author: dongyang_wu
 * @create: 2019-07-30 18:49
 */
@Component
public class LdapConfiguration {
    @Bean
    public LdapUtil getLdapUtil() {
        return new LdapUtil();
    }}