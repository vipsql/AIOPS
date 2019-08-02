package com.coocaa.data.manipulation.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * @Auther: wyx
 * @Date: 2019-07-22 15:19
 * @Description: 配置 WebSocket
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * 注册协议节点
     * 映射指定的 URL
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/fetchData").setAllowedOrigins("*").withSockJS();
    }

    /**
     * 配置消息代理
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 广播消息代理
        registry.enableSimpleBroker("/monitoring");
        // 全局使用的订阅前缀
        registry.setApplicationDestinationPrefixes("/app/");
    }

}
