package com.coocaa.prometheus.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 指标集清单
 */
public class KPIListing {

    public static Map<String, String> KPI = new HashMap<>();

    static {
        KPI.put("请求总数", "http_requests_total%s");
        KPI.put("CPU-1分钟负载", "node_load1%s");
        KPI.put("CPU-5分钟负载", "node_load5%s");
        KPI.put("CPU-15分钟负载", "node_load15%s");
        KPI.put("磁盘总容量", "node_filesystem_size_bytes%s");
        KPI.put("磁盘剩余容量", "node_filesystem_free_bytes%s");
        KPI.put("磁盘使用率", "node_filesystem_used_rate%s");
        KPI.put("上传带宽", "sum(irate(node_network_receive_bytes_total{device!~\"bond.*?|lo\"}[5m])/128)");
        KPI.put("下载带宽", "sum(irate(node_network_transmit_bytes_total{device!~\"bond.*?|lo\"}[5m])/128)");
        KPI.put("TCP连接情况", "node_sockstat_TCP_alloc%s");
        KPI.put("CPU使用率", "irate(node_cpu_seconds_total%s[1m])");
        KPI.put("磁盘每秒的I/O操作耗费时间(%)", "irate(node_disk_io_time_seconds_total%s[1m])");
        KPI.put("网络进带宽", "irate(node_network_receive_bytes_total%s[5m])*8");
        KPI.put("磁盘写入速率", "irate(node_disk_write_time_seconds_total%s[1m])");
        KPI.put("磁盘IO加权", "irate(node_disk_io_time_weighted_seconds_total%s[1m])");
        KPI.put("磁盘IO速率", "irate(node_disk_io_time_seconds_total%s[1m])");
        KPI.put("内存Cache容量", "node_memory_Cached_bytes%s");
        KPI.put("内存使用率", "((node_memory_MemTotal_bytes%s - node_memory_MemFree_bytes%s - node_memory_Buffers_bytes%s - node_memory_Cached_bytes%s) / (node_memory_MemTotal_bytes%s )) * 100");
    }
}
