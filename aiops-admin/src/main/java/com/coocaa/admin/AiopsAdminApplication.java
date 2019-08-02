package com.coocaa.admin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

@EnableAdminServer
@SpringCloudApplication
public class AiopsAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiopsAdminApplication.class, args);
    }

}
