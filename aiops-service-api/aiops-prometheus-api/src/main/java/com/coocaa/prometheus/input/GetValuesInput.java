package com.coocaa.prometheus.input;

import lombok.*;

import java.util.Map;

/**
 * @description: 获取数据接口用户输入实体
 * @author: dongyang_wu
 * @create: 2019-08-06 21:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetValuesInput {
    private String metricsName;
    private Long dateTime;
    private Integer span;
    private Integer step;
    Map<String, String> conditions;
}