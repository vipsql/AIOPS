package com.coocaa.detector.entity;

import lombok.Data;

/**
 * @author 陈煜坤
 * @date 2019/8/8  11:04
 * @package_name com.monitoring.warn.notice.entity
 */
@Data
public class Count {

    private int total_count;
    private int negative_count;
    private int positive_count;
}
