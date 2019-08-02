package com.coocaa.prometheus.input;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

/**
 * @program: intelligent_maintenance
 * @description: http请求数查询
 * @author: dongyang_wu
 * @create: 2019-08-02 13:40
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QueryMetricProperty {
    /**
     * 通用属性
     */
    private String metricsName;
    private String instance;
    /**
     * http请求数属性
     */
    private String request;
    private String status;

    public interface HttpRequestsTotalNameKey {
        String INSTANCE = "instance";
        String REQUEST = "request";
        String STATUS = "status";
    }
}