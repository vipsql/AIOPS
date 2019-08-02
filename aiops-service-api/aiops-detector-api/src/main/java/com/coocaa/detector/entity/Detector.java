package com.coocaa.detector.entity;

import lombok.*;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * @author 陈煜坤
 * @date 2019/7/31  9:27
 * @package_name com.common.parent_model.entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Detector implements Serializable {

    /**
     * 视图 id  长度31
     */
    private String viewId;
    /**
     * 视图名字 长度63
     */
    private String viewName;
    /**
     * 属性id  长度31
     */
    private String attrId;
    /**
     * 属性名字 长度63
     */
    private String attrName;
    /**
     * 使用模型ID 默认 使用自带的  xgb_default
     */
    private String taskId = "xgb_default";
    /**
     *  检测区级间隔，暂定规定间隔为 180个数据
     */
    private int window = 180;
    /**
     * 检测数据的时间点  格式为 2019-06-06 11:00:00
     */
    private String time;
    /**
     * 检测的数据的上周的数据【1 - 361】 ==》【前180个数据 +（检测数据）+ 后180个数据】
     */
    private String dataC;
    /**
     * 检测的数据的昨天的数据【1 - 361】 ==》【前180个数据 +（检测数据）+ 后180个数据】
     */
    private String dataB;
    /**
     * 检测的数据【1 - 181】 ==》 【前180个数据 + （检测数据） 】
     */
    private String dataA;

    public boolean isSomeEmtry(){
        if (StringUtils.isEmpty(this.attrId) &&
                StringUtils.isEmpty(this.attrName) &&
                StringUtils.isEmpty(this.dataA) &&
                StringUtils.isEmpty(this.dataB) &&
                StringUtils.isEmpty(this.dataC) &&
                StringUtils.isEmpty(this.time) &&
                StringUtils.isEmpty(this.viewId) &&
                StringUtils.isEmpty(this.viewName) &&
                StringUtils.isEmpty(this.window+"") &&
                StringUtils.isEmpty(this.taskId)){

            return false;
        }
        return true;
    }

}