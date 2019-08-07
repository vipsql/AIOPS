package com.coocaa.prometheus.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private Date start;
    @JsonIgnore
    private Date end;
    private String query;
    private Integer span;
    private Integer step;
    private Map<String, String> conditions;
}