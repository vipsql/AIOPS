package com.coocaa.gateway.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.*;
import reactor.core.publisher.Mono;

 /**
 * @program: intelligent_maintenance
 * @description: Hystrix 降级处理
 * @author: dongyang_wu
 * @create: 2019-07-28 21:25
 */
@Slf4j
@Component
public class HystrixFallbackHandler implements HandlerFunction<ServerResponse> {
	@Override
	public Mono<ServerResponse> handle(ServerRequest serverRequest) {
		log.error("网关执行请求:{}失败,hystrix服务降级处理", serverRequest.uri());
		return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
			.contentType(MediaType.TEXT_PLAIN).body(BodyInserters.fromObject("服务异常"));
	}
}
