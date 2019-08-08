package com.coocaa.prometheus.output;

import com.coocaa.prometheus.input.MetisCsvInputVo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

/**
 * @description: 训练数据输出实体类
 * @author: dongyang_wu
 * @create: 2019-08-08 13:28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class MetisCsvOutputVo {
    @ApiModelProperty("导出训练数据输入Vo")
    private MetisCsvInputVo metisCsvInputVo;
    @ApiModelProperty("导出训练数据接口返回的接口集")
    private List<MetricsCsvVo> metricsCsvVos;
}