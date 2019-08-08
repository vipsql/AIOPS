package com.coocaa.detector.entity;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 训练类
 *
 * @author 陈煜坤
 * @date 2019/8/7  15:15
 * @package_name com.common.parent_model.entity
 */
@Data
@Builder
@AllArgsConstructor
public class Train {

    /**
     * 训练数据的起始时间，为时间戳
     */
    private Long beginTime;
    /**
     * 训练数据的结尾时间，为时间戳
     */
    private Long endTime;
    /**
     * 无
     */
    private String positiveOrNegative = "";
    /**
     * 数据集来源，只有 test 和 train
     */
    private List<String> trainOrTest;
    /**
     * 资源集来源，来源与其他位置
     */
    private List<String> source;
    /**
     * 模型名称，自定义
     */
    private String model_name;

    public Train() {
        if (trainOrTest == null) {
            trainOrTest = new ArrayList<>();
        }
        if (source == null) {
            source = new ArrayList<>();
        }
    }
}
