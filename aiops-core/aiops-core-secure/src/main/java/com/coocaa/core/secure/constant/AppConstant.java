package com.coocaa.core.secure.constant;

/**
 * @description: 系统常量
 * @author: dongyang_wu
 * @create: 2019-07-28 21:11
 */
public interface AppConstant {
    /**
     * 应用版本
     */
    String APPLICATION_VERSION = "2.3.1";
    /**
     * 基础包
     */
    String BASE_PACKAGES = "com.coocaa";
    /**
     * 应用名前缀
     */
    String APPLICATION_NAME_PREFIX = "aiops-";
    /**
     * 网关模块名称
     */
    String APPLICATION_GATEWAY_NAME = APPLICATION_NAME_PREFIX + "gateway";
    /**
     * 授权模块名称
     */
    String APPLICATION_AUTH_NAME = APPLICATION_NAME_PREFIX + "auth";
    /**
     * 用户模块名称
     */
    String APPLICATION_USER_NAME = APPLICATION_NAME_PREFIX + "user";
    /**
     * 日志模块名称
     */
    String APPLICATION_LOG_NAME = APPLICATION_NAME_PREFIX + "log";
    /**
     * 监控模块名称
     */
    String APPLICATION_ADMIN_NAME = APPLICATION_NAME_PREFIX + "admin";
    /**
     * 邮件模块名称
     */
    String APPLICATION_NOTICE_NAME = APPLICATION_NAME_PREFIX + "notice";
    /**
     * 异常检测模块名称
     */
    String APPLICATION_DETECTOR_NAME = APPLICATION_NAME_PREFIX + "detector";
    /**
     * 定时任务模块名称
     */
    String APPLICATION_TASK_NAME = APPLICATION_NAME_PREFIX + "prometheus";
    /**
     * 开发环境
     */
    String DEV_CODE = "dev";
    /**
     * 生产环境
     */
    String PROD_CODE = "prod";
    /**
     * 测试环境
     */
    String TEST_CODE = "test";

    /**
     * 代码部署于 linux 上，工作默认为 mac 和 Windows
     */
    String OS_NAME_LINUX = "LINUX";
    /**
     * 用户token过期时间
     */
    Long TOKEN_EXPIRE_TIME = 6 * 10000000L;
    /**
     * Feign内部调用过期时间
     */
    Long FEIGN_TOKEN_EXPIRE_TIME = 6 * 1000L;
}