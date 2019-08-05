package com.coocaa.prometheus.entity;

import lombok.*;

/**
 * @Auther: wyx
 * @Date: 2019-07-27 12:41
 * @Description: Prometheus 指标
 */
@Builder
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Metric {

    private String __name__;
    private String instance;
    private String job;
    private String module;
    private String system;
    private String type;
    private String device;

    /**
     * 分区类型
     */
    private String fstype;

    /**
     * 挂载点
     */
    private String mountpoint;

    /**
     * http请求属性
     */
    private String request;
    private String status;
}
