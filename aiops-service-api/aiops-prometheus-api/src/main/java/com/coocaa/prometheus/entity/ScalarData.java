package com.coocaa.prometheus.entity;

import lombok.*;

import java.util.List;

/**
 * @Auther: wyx
 * @Date: 2019-07-27 14:20
 * @Description: 标量数据
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ScalarData {

    /**
     * [ <unix_time>, "<scalar_value>" ]
     */
    private List<String> value;

}
