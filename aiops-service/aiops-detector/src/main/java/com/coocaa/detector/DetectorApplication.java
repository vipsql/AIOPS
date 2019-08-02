package com.coocaa.detector;

import com.coocaa.core.secure.constant.AppConstant;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @program: intelligent_maintenance
 * @description: 数据检测服务启动器
 * @author: dongyang_wu
 * @create: 2019-07-31 11:46
 */
@SpringBootApplication(scanBasePackages = {"com.coocaa"})
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableFeignClients(AppConstant.BASE_PACKAGES)
public class DetectorApplication {
    public static void main(String[] args) {
        SpringApplication.run(DetectorApplication.class, args);
    }

}