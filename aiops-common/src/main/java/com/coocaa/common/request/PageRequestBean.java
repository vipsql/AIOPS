package com.coocaa.common.request;

import lombok.*;

import java.util.List;

/**
 * @description: 请求分页参数
 * @author: dongyang_wu
 * @create: 2019-08-06 09:58
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageRequestBean {
    private Integer page = 0;
    private Integer count = 5;
    private String conditionConnection;
    private String orderBy;
    private String sortType;
    private List<PageRequestItem> conditions;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PageRequestItem {
        private String query;
        private String connection;
        private String queryString;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class PageTeamRequestItem {
        private String connection;
        private String queryString;
    }
}