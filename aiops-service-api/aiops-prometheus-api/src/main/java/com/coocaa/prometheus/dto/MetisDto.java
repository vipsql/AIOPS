package com.coocaa.prometheus.dto;

import lombok.*;

/**
 * @description: Metis训练数据DTO
 * @author: dongyang_wu
 * @create: 2019-08-07 16:47
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MetisDto {
    /**
     * 指标集ID
     */
    private Long viewId;
    /**
     * 指标集名
     */
    private String viewName;
    /**
     * 指标ID
     */
    private Long attrId;
    /**
     * 指标名
     */
    private String attrName;
    /**
     * 模型名
     */
    private String modelName;
    /**
     * 样本来源
     */
    private String source;
    /**
     * 测试还是训练
     */
    private String trainOrTest;
}