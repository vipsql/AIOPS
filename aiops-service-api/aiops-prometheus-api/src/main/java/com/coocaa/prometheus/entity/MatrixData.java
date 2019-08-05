package com.coocaa.prometheus.entity;

import com.coocaa.detector.entity.DetectorResult;
import lombok.*;

import java.util.List;

/**
 * @Auther: wyx
 * @Date: 2019-07-27 14:23
 * @Description: 区间向量
 */
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MatrixData {
    private Metric metric;

    /**
     * [ [ <unix_time>, "<sample_value>" ] ]
     */
    private List<String> values;
    private DetectorResult.DataBean detectResult;
    private String metisData;
}
