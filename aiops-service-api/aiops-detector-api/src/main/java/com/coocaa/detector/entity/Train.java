package com.coocaa.detector.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.coocaa.core.tool.utils.CollectionUtil;
import lombok.*;

import java.util.*;

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

    private int id;
    /**
     * 训练数据的起始时间，为时间戳
     */
    private long beginTime;
    /**
     * 训练数据的结尾时间，为时间戳
     */
    private long endTime;
    /**
     * 无
     */
    private String positiveOrNegative = "";
    /**
     * 数据集来源，只有 test 和 train
     */
    @TableField(exist = false)
    private List<String> trainOrTest;
    /**
     * 资源集来源，来源与其他位置
     */
    @TableField(exist = false)
    private List<String> source;

    /**
     * 數據
     */
    @TableField("source")
    private String data;

    /**
     * 模型名称，自定义
     */
    private String modelName;

    /**
     * 模型状态
     */
    private String stutas = "mark";

    /**
     * 时间间隔
     */
    private int timeInterval;

    /**
     * 预计时间
     */
    private int exTime;

    public Train() {
        if (trainOrTest == null) {
            trainOrTest = new ArrayList<>();
            trainOrTest.add("train");
            trainOrTest.add("test");
        }
        if (source == null) {
            source = new ArrayList<>();
        }
    }


    public void setTrainOrTest(String str) {
        String[] split = str.split(",");
        if (split.length > 0) {
            for (String s : split) {
                trainOrTest.add(s);
            }
        }
    }

    public void setData(String data) {
        this.data = data;
        setSource(data);
    }

    public void setSource(String str) {
        if (CollectionUtil.isEmpty(source))
            source = new ArrayList<>();
        String[] split = str.split(",");
        if (split.length > 0) {
            source.addAll(Arrays.asList(split));
        }
    }


}
