package com.coocaa.prometheus.entity;

import lombok.*;

import java.util.List;

/**
 * @Auther: wyx
 * @Date: 2019-07-29 9:37
 * @Description: 区间数据
 */
@Builder
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PromMatrixData {

    private List<MatrixData> requests;

    private List<MatrixData> disk;

    private List<MatrixData> cpuLoad;

    private List<MatrixData> upLoadBandWidth;

    private List<MatrixData> downLoadBandWidth;

}
