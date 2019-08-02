package com.coocaa.gateway.provider;

import com.coocaa.common.constant.AppConstant;
import lombok.AllArgsConstructor;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.support.NameUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.*;

/**
 * @program: intelligent_maintenance
 * @description: 聚合文档注册
 * @author: dongyang_wu
 * @create: 2019-07-28 21:11
 */
@Primary
@Component
@AllArgsConstructor
public class SwaggerProvider implements SwaggerResourcesProvider {
    public static final String API_URI = "/v2/api-docs-ext";
    //    public static final String API_URI = "/v2/api-docs";
    private final RouteLocator routeLocator;
    private final GatewayProperties gatewayProperties;
    private static Map<String, String> routeMap = new HashMap<>();

    static {
        routeMap.put(AppConstant.APPLICATION_AUTH_NAME, "授权服务");
//		routeMap.put(AppConstant.APPLICATION_DESK_NAME, "工作台模块");
//		routeMap.put(AppConstant.APPLICATION_SYSTEM_NAME, "系统模块");
//		routeMap.put(AppConstant.APPLICATION_USER_NAME, "用户模块");
//		routeMap.put(AppConstant.APPLICATION_FILE_PPT_PDF_NAME, "PPTPDF文件服务");
        routeMap.put(AppConstant.APPLICATION_USER_NAME, "用户服务");
        routeMap.put(AppConstant.APPLICATION_NOTICE_NAME, "邮件通知服务");
        routeMap.put(AppConstant.APPLICATION_DETECTOR_NAME, "异常检测服务");
    }

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        List<String> routes = new ArrayList<>();
        routeLocator.getRoutes().subscribe(route -> routes.add(route.getId()));
        gatewayProperties.getRoutes().stream().filter(routeDefinition -> routes.contains(routeDefinition.getId()))
                .forEach(routeDefinition -> routeDefinition.getPredicates().stream()
                        .filter(predicateDefinition -> "Path".equalsIgnoreCase(predicateDefinition.getName()))
                        .forEach(predicateDefinition -> resources.add(swaggerResource(routeDefinition.getId(),
                                predicateDefinition.getArgs().get(NameUtils.GENERATED_NAME_PREFIX + "0")
                                        .replace("/**", API_URI)))));
        return resources;
    }


    private SwaggerResource swaggerResource(String name, String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName((routeMap.get(name) == null ? name : routeMap.get(name)));
        swaggerResource.setLocation(location);
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }

}
