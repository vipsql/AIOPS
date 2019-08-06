package com.coocaa.core.secure.config;


import com.coocaa.core.secure.interceptor.SecureInterceptor;
import com.coocaa.core.secure.registry.SecureRegistry;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 安全配置类
 *
 * @author dongyang_wu
 */
@Order
@Configuration
@AllArgsConstructor
public class SecureConfiguration implements WebMvcConfigurer {
    private SecureRegistry secureRegistry;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        if (secureRegistry.isEnable()) {
            registry.addInterceptor(new SecureInterceptor())
                    .excludePathPatterns(secureRegistry.getExcludePatterns())
                    .excludePathPatterns(secureRegistry.getDefaultExcludePatterns());
        }
    }


}
