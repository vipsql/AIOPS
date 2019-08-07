package com.coocaa.core.boot.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryInterceptorBuilder;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

/**
 * 重试机制
 *
 * @author dongyang_wu
 */
@Slf4j
@Configuration
public class RetryConfiguration {

	@Bean
	@ConditionalOnMissingBean(name = "configServerRetryInterceptor")
	public RetryOperationsInterceptor configServerRetryInterceptor() {
		log.info(String.format(
			"configServerRetryInterceptor: Changing backOffOptions " +
				"to initial: %s, multiplier: %s, maxInterval: %s",
			1000, 1.2, 5000));
		return RetryInterceptorBuilder
			.stateless()
			.backOffOptions(1000, 1.2, 5000)
			.maxAttempts(10)
			.build();
	}

}
