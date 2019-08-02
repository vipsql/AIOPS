package com.monitoring.data_manipulation.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Auther: wyx
 * @Date: 2019-07-25 11:38
 * @Description: RabbitMQ 配置
 */
@Configuration
public class RabbitConfig {

    @Bean
    public Queue Queue() {
        return new Queue("timingData");
    }

}
