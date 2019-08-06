package com.coocaa.prometheus.output;

import lombok.*;

import java.util.Date;

/**
 * @description: 指标训练实体
 * @author: dongyang_wu
 * @create: 2019-08-06 14:11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MetricsCsvVo {
    private Long viewId;
    private String viewName;
    private Long attrId;
    private String attrName;
    private String source;
    private String trainOrTest;
    private String positiveOrNegative;
    private Long dateTime;
    private String window;
    private String dataA;
    private String dataB;
    private String dataC;
}