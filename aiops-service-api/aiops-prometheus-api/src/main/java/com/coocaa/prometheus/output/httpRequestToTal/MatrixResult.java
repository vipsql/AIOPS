package com.coocaa.prometheus.output.httpRequestToTal;

import com.coocaa.detector.entity.DetectorResult;
import com.coocaa.prometheus.entity.Metric;
import lombok.*;

import java.util.List;

/**
 * @description: 异常检测结果
 * @author: dongyang_wu
 * @create: 2019-08-02 15:52
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MatrixResult {
    private Metric metric;
    private DetectorResult.DataBean detectResult;
}