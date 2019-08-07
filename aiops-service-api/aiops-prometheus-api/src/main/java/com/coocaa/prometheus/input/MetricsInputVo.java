package com.coocaa.prometheus.input;

import com.coocaa.prometheus.entity.QueryRange;
import lombok.*;

/**
 * @description: 指标用户输入类
 * @author: dongyang_wu
 * @create: 2019-08-05 15:39
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MetricsInputVo {
    // 指标集属性开始
    private Long id;
    private String metricName;
    /**
     * team id集合
     */
    private String teamIds;

    // 指标集属性结束


    // 指标属性开始
    /**
     * 指标所对应的定时任务Id
     */
    private Long taskId;
    /**
     * 指标名
     */
    private String taskName;
    /**
     * 查询条件JSON字符串
     */
    private QueryRange queryRange;
    /**
     * 定时拉取数据频率
     */
    private String taskCron;
    /**
     * 模型名
     */
    private String modelName;
    // 指标属性结束
}