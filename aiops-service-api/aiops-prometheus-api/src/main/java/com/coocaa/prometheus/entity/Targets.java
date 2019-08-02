package com.coocaa.prometheus.entity;

import lombok.*;

/**
 * @program: intelligent_maintenance
 * @description: 监控项
 * @author: dongyang_wu
 * @create: 2019-08-02 13:01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Targets {
    /**
     * labels : {"instance":"172.16.33.8:3903","job":"adsys","module":"tv-monitor","system":"adsys","type":"nginx"}
     * scrapeUrl : http://172.16.33.8:3903/metrics
     * lastError :
     * lastScrape : 2019-08-02T12:56:49.894858032+08:00
     * health : up
     */
    private LabelsBean labels;
    private String scrapeUrl;
    private String lastError;
    private String lastScrape;
    private String health;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LabelsBean {
        /**
         * instance : 172.16.33.8:3903
         * job : adsys
         * module : tv-monitor
         * system : adsys
         * type : nginx
         */

        private String instance;
        private String job;
        private String module;
        private String system;
        private String type;

    }
}