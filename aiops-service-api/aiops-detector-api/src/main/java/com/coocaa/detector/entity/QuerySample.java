package com.coocaa.detector.entity;

import lombok.Data;

/**
 * @author 陈煜坤
 * @date 2019/8/16  17:04
 * @package_name com.common.parent_model.entity
 */
@Data
public class QuerySample {

    private String attrId;
    private long beginTime;
    private long endTime;
    private int size;
    private String positiveOrNegative = "";
    private int current;
    private String data = "";
    private String trainOrTest = "";
    private String viewId;
    private String window;
}
