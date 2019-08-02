package com.coocaa.data.manipulation;

import com.coocaa.core.secure.constant.AppConstant;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.coocaa"})
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableFeignClients(AppConstant.BASE_PACKAGES)
@EnableRabbit
@EnableScheduling
public class DataManipulationApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataManipulationApplication.class, args);
    }

}
