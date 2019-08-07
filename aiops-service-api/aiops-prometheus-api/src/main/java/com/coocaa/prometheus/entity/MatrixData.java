package com.coocaa.prometheus.entity;

import com.coocaa.detector.entity.DetectorResult;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private String metisData;


    public String specialKey() {
        StringBuffer specialKey = new StringBuffer();
        specialKey.append("instance:").append(metric.getInstance()).append("-")
                // http请求数
                .append("request:").append(metric.getRequest() == null ? "" : metric.getRequest()).append("-")
                .append("status:").append(metric.getStatus() == null ? "" : metric.getStatus()).append("-")
                // 磁盘相关
                .append("device:").append(metric.getDevice() == null ? "" : metric.getDevice()).append("-")
                .append("mountpoint:").append(metric.getMountpoint() == null ? "" : metric.getMountpoint()).append("-")
                .append("fstype:").append(metric.getFstype() == null ? "" : metric.getFstype()).append("-")
                // CPU使用率
                .append("mode:").append(metric.getMode() == null ? "" : metric.getMode()).append("-")
                .append("cpu:").append(metric.getCpu() == null ? "" : metric.getCpu());
        return specialKey.toString();
    }
}
