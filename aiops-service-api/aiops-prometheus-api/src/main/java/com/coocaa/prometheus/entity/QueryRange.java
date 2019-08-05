package com.coocaa.prometheus.entity;

import lombok.*;

import java.util.Date;
import java.util.Map;

/**
 * @program: intelligent_maintenance
 * @description: 范围查询
 * @author: dongyang_wu
 * @create: 2019-08-01 15:20
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryRange {
    private String query;
    private Date start;
    private Date end;
    private Integer span;
    private Integer step;
    private Map<String, String> conditions;
}