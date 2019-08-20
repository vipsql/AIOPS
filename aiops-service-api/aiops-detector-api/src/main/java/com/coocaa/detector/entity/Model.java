package com.coocaa.detector.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 陈煜坤
 * @date 2019/8/12  10:33
 * @package_name com.monitoring.warn.notice.entity
 */
@Data
public class Model implements Serializable {

    private int id;
    private String taskId;
    private int sampleNum;
    private int postiveSampleNum;
    private int negativeSampleNum;
    private int window;
    private String modelName;
    private String source;
    private Date startTime;
    private Date endTime;
    private String status;
}
