package com.coocaa.core.swagger;


import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.*;

/**
 * @program: intelligent_maintenance
 * @description: swagger配置
 * @author: dongyang_wu
 * @create: 2019-07-28 21:11
 */
@Configuration
@EnableSwagger2
@Profile({"dev", "test", "prod"})
@ConditionalOnExpression("${swagger.enable:false}")
@EnableConfigurationProperties(SwaggerProperties.class)
public class SwaggerAutoConfiguration {

    private static final String DEFAULT_EXCLUDE_PATH = "/error";
    private static final String BASE_PATH = "/**";

    @Bean
    @ConditionalOnMissingBean
    public SwaggerProperties swaggerProperties() {
        return new SwaggerProperties();
    }

    @Bean
    public Docket api(SwaggerProperties swaggerProperties) {
        // base-path处理
        if (swaggerProperties.getBasePath().size() == 0) {
            swaggerProperties.getBasePath().add(BASE_PATH);
        }
        //noinspection unchecked
        List<Predicate<String>> basePath = new ArrayList();
        swaggerProperties.getBasePath().forEach(path -> basePath.add(PathSelectors.ant(path)));

        // exclude-path处理
        if (swaggerProperties.getExcludePath().size() == 0) {
            swaggerProperties.getExcludePath().add(DEFAULT_EXCLUDE_PATH);
        }
        List<Predicate<String>> excludePath = new ArrayList<>();
        swaggerProperties.getExcludePath().forEach(path -> excludePath.add(PathSelectors.ant(path)));
        //可以添加多个header或参数
        ParameterBuilder AUTHORIZATION = new ParameterBuilder();
        AUTHORIZATION.parameterType("header")
                .name("AIOPS-TOKEN")
                .description("header中AIOPS-TOKEN字段用于使用API的验证")
                .modelRef(new ModelRef("string"))
                //非必需，这里是全局配置，然而在登陆的时候是不用验证的
                .required(false).build();
        List<Parameter> aParameters = new ArrayList<>();
        aParameters.add(AUTHORIZATION.build());
        //noinspection Guava
        return new Docket(DocumentationType.SWAGGER_2)
                .host(swaggerProperties.getHost())
                .apiInfo(apiInfo(swaggerProperties)).select()
                .apis(RequestHandlerSelectors.basePackage(swaggerProperties.getBasePackage()))
                .paths(Predicates.and(Predicates.not(Predicates.or(excludePath)), Predicates.or(basePath)))
                .build()
                .securitySchemes(Collections.singletonList(securitySchema()))
                .securityContexts(Collections.singletonList(securityContext()))
                .pathMapping("/")
                .globalOperationParameters(aParameters);
    }

    /**
     * 配置默认的全局鉴权策略的开关，通过正则表达式进行匹配；默认匹配所有URL
     *
     * @return
     */
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex(swaggerProperties().getAuthorization().getAuthRegex()))
                .build();
    }

    /**
     * 默认的全局鉴权策略
     *
     * @return
     */
    private List<SecurityReference> defaultAuth() {
        ArrayList<AuthorizationScope> authorizationScopeList = new ArrayList<>();
        swaggerProperties().getAuthorization().getAuthorizationScopeList().forEach(authorizationScope -> authorizationScopeList.add(new AuthorizationScope(authorizationScope.getScope(), authorizationScope.getDescription())));
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[authorizationScopeList.size()];
        return Collections.singletonList(SecurityReference.builder()
                .reference(swaggerProperties().getAuthorization().getName())
                .scopes(authorizationScopeList.toArray(authorizationScopes))
                .build());
    }


    private OAuth securitySchema() {
        ArrayList<AuthorizationScope> authorizationScopeList = new ArrayList<>();
        swaggerProperties().getAuthorization().getAuthorizationScopeList().forEach(authorizationScope -> authorizationScopeList.add(new AuthorizationScope(authorizationScope.getScope(), authorizationScope.getDescription())));
        ArrayList<GrantType> grantTypes = new ArrayList<>();
        swaggerProperties().getAuthorization().getTokenUrlList().forEach(tokenUrl -> grantTypes.add(new ResourceOwnerPasswordCredentialsGrant(tokenUrl)));
        return new OAuth(swaggerProperties().getAuthorization().getName(), authorizationScopeList, grantTypes);
    }

    private ApiInfo apiInfo(SwaggerProperties swaggerProperties) {
        return new ApiInfoBuilder()
                .title(swaggerProperties.getTitle())
                .description(swaggerProperties.getDescription())
                .license(swaggerProperties.getLicense())
                .licenseUrl(swaggerProperties.getLicenseUrl())
                .termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl())
                .contact(new Contact(swaggerProperties.getContact().getName(), swaggerProperties.getContact().getUrl(), swaggerProperties.getContact().getEmail()))
                .version(swaggerProperties.getVersion())
                .build();
    }

}
