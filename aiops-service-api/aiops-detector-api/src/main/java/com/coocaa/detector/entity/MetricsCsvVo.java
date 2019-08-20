package com.coocaa.detector.entity;

import lombok.*;

import java.io.Serializable;

/**
 * @author 陈煜坤
 * @date 2019/8/7  16:31
 * @package_name com.monitoring.warn.notice.entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MetricsCsvVo implements Serializable {
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