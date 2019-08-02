package com.coocaa.prometheus;

import com.coocaa.core.secure.constant.AppConstant;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @program: intelligent_maintenance
 * @description:
 * @author: dongyang_wu
 * @create: 2019-07-31 22:31
 */
@SpringBootApplication(scanBasePackages = {"com.coocaa"})
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableFeignClients(AppConstant.BASE_PACKAGES)
@EnableRabbit
public class PrometheusApplication {
    public static void main(String[] args) {
        SpringApplication.run(PrometheusApplication.class, args);
    }

}