package com.coocaa.prometheus.common;

/**
 * @Auther: wyx
 * @Date: 2019-07-27 23:44
 * @Description: 要监控的指标
 */
public class PromNorm {
    /**
     * 请求总数
     */
    public static final String REQUEST_TOTAL = "http_requests_total%s";

    /**
     * CPU 一分钟内平均负载
     */
    public static final String CPU_LOAD = "node_load1%s";

    /**
     * 磁盘总容量
     */
    public static final String DISK_TOTAL_CAPACITY = "node_filesystem_size_bytes%s";

    /**
     * 磁盘剩余容量
     */
    public static final String DISK_FREE_CAPACITY = "node_filesystem_free_bytes%s";

    /**
     * 磁盘使用率
     */
    public static final String DISK_USED_RATE = "node_filesystem_used_rate%s";

    /**
     * 上传带宽
     */
    public static final String UPLOAD_BAND_WIDTH = "sum(irate(node_network_receive_bytes_total{device!~\"bond.*?|lo\"}[5m])/128)";

    /**
     * 下载带宽
     */
    public static final String DOWNLOAD_BAND_WIDTH = "sum(irate(node_network_transmit_bytes_total{device!~\"bond.*?|lo\"}[5m])/128)";
    /**
     * TCP连接情况
     */
    public static final String NODE_SOCKSTAT_TCP_ALLOC = "node_sockstat_TCP_alloc%s";
    /**
     * CPU使用率
     */
    public static final String NODE_CPU_SECONDS_TOTAL = "irate(node_cpu_seconds_total%s[1m])";
    /**
     * 磁盘每秒的I/O操作耗费时间（%）
     */
    public static final String NODE_DISK_IO_TIME_SECONDS_TOTAL = "irate(node_disk_io_time_seconds_total%s[1m])";
    /**
     * 网络进带宽
     */
    public static final String NODE_NETWORK_RECEIVE_BYTES_TOTAL = "irate(node_network_receive_bytes_total%s[5m])*8";
    /**
     * 内存cache node_memory_Cached_bytes
     */
    public static final String NODE_MEMORY_CACHED_BYTES = "node_memory_Cached_bytes%s";

}
