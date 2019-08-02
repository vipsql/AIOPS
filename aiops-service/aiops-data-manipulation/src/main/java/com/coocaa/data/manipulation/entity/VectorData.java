package com.monitoring.data_manipulation.entity;

import lombok.*;

import java.util.List;

/**
 * @Auther: wyx
 * @Date: 2019-07-27 14:17
 * @Description: 瞬时向量
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VectorData {

    private Metric metric;

    /**
     * [ <unix_time>, "<sample_value>" ]
     */
    private List<String> value;

}
