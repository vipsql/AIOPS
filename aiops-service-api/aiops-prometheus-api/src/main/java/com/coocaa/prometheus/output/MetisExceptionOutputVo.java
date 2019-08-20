package com.coocaa.prometheus.output;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;
import java.util.Map;

/**
 * @author: dongyang_wu
 * @create: 2019-08-16 15:11
 * @description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MetisExceptionOutputVo {
    private Long id;
    @ApiModelProperty("异常所属指标Id")
    private Map<Long, Object> taskToStringMap;
    @ApiModelProperty("最近处理人用户")
    private Map<Long, Object> recentUserIdMap;
    @ApiModelProperty("异常数据List JSON字符串")
    private String matrixDataJson;
    @ApiModelProperty("0未处理1已修正2已恢复3已修复")
    private Integer status;
    @ApiModelProperty("处理人及理由JSON字符串")
    private String userToReasonJson;
    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;
    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;
}