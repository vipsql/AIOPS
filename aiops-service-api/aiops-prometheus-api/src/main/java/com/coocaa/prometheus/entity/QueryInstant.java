package com.coocaa.prometheus.entity;

import lombok.*;

import java.util.Date;

/**
 * @program: intelligent_maintenance
 * @description: 即时查询
 * @author: dongyang_wu
 * @create: 2019-08-01 15:18
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueryInstant {

    private String query;
    private Date date;
    private Integer timeout;
}