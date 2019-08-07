package com.coocaa.core.log.config;

import com.coocaa.core.log.aspect.ApiLogAspect;
import com.coocaa.core.log.event.*;
import com.coocaa.core.log.logger.AiOpsLogger;
import com.coocaa.core.log.props.AiopsProperties;
import com.coocaa.core.log.server.ServerInfo;
import com.coocaa.core.log.feign.ILogClient;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 日志工具自动配置
 *
 * @author dongyang_wu
 */
@Configuration
@AllArgsConstructor
@ConditionalOnWebApplication
public class AiopsLogToolAutoConfiguration {

	private final ILogClient logService;
	private final ServerInfo serverInfo;
	private final AiopsProperties aiopsProperties;

	@Bean
	public ApiLogAspect apiLogAspect() {
		return new ApiLogAspect();
	}

	@Bean
	public AiOpsLogger bladeLogger() {
		return new AiOpsLogger();
	}

	@Bean
	public ApiLogListener apiLogListener() {
		return new ApiLogListener(logService, serverInfo, aiopsProperties);
	}

	@Bean
	public ErrorLogListener errorEventListener() {
		return new ErrorLogListener(logService, serverInfo, aiopsProperties);
	}

	@Bean
	public UsualLogListener bladeEventListener() {
		return new UsualLogListener(logService, serverInfo, aiopsProperties);
	}

}
