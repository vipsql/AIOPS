package com.monitoring.data_manipulation.common;

/**
 * @Auther: wyx
 * @Date: 2019-07-27 23:44
 * @Description: 要监控的指标
 */
public class PromNorm {

    /**
     * 请求总数
     */
    public static final String REQUEST_TOTAL = "http_requests_total";

    /**
     * CPU 一分钟内平均负载
     */
    public static final String CPU_LOAD = "node_load1";

    /**
     * 磁盘总容量
     */
    public static final String DISK_TOTAL_CAPACITY = "node_filesystem_size_bytes";

    /**
     * 磁盘剩余容量
     */
    public static final String DISK_FREE_CAPACITY = "node_filesystem_free_bytes";

    /**
     * 磁盘使用率
     */
    public static final String DISK_USED_RATE = "node_filesystem_used_rate";

    /**
     * 上传带宽
     */
    public static final String UPLOAD_BAND_WIDTH = "sum(irate(node_network_receive_bytes_total{device!~\"bond.*?|lo\"}[5m])/128)";

    /**
     * 下载带宽
     */
    public static final String DOWNLOAD_BAND_WIDTH = "sum(irate(node_network_transmit_bytes_total{device!~\"bond.*?|lo\"}[5m])/128)";

}
