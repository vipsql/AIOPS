package com.coocaa.prometheus.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;

/**
 * @description: 导出Metis训练数据用户输入实体
 * @author: dongyang_wu
 * @create: 2019-08-06 14:42
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class MetisCsvInputVo {
    @ApiModelProperty("训练数据开始时间")
    private Date begin;
    @ApiModelProperty("训练数据结束时间")
    private Date end;
    @ApiModelProperty("跨度，以分钟为单位")
    private Integer span;
    @ApiModelProperty("样本来源")
    private String source;
    @ApiModelProperty("训练集还是测试集")
    private String trainOrTest;
    @ApiModelProperty("对应指标Id")
    private Long taskId;
}