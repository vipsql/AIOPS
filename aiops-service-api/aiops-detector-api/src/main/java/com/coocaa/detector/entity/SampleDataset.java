package com.coocaa.detector.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author 陈煜坤
 * @date 2019/8/19  10:56
 * @package_name com.monitoring.warn.notice.entity
 */
@Data
public class SampleDataset {
    private int id;
    private Date updateTime;
    private String viewId;
    private String viewName;
    private String attrName;
    private String attrId;
    private String source;
    private String trainOrTest;
    private String positiveOrNegative;
    private int window;
    private int dataTime;
    private String dataC;
    private String dataB;
    private String dataA;
    private int anomalyId;
}
