package com.coocaa.common.request;

import lombok.*;

import java.util.List;

/**
 * @program: intelligent_maintenance
 * @description: 请求体
 * @author: dongyang_wu
 * @create: 2019-08-01 16:46
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RequestBean {
    private List<RequestItem> items;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RequestItem {
        private String query;
        private String queryString;
    }
}