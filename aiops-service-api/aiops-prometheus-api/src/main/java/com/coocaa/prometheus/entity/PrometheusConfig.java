package com.coocaa.prometheus.entity;

import lombok.*;

import java.util.List;

/**
 * @program: intelligent_maintenance
 * @description:
 * @author: dongyang_wu
 * @create: 2019-07-31 21:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrometheusConfig {
    private LabelsBean labels;
    private List<String> targets;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class LabelsBean {
        private String instance;
    }
}


