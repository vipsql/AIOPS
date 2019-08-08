package com.coocaa.prometheus.input;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @description: MetisException用户输入类
 * @author: dongyang_wu
 * @create: 2019-08-07 15:01
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel
public class MetisExceptionInputVo {
    private Long id;
    @ApiModelProperty("0未处理1已修正2已恢复3已修复")
    private Integer status;
    @ApiModelProperty("用户输入的理由")
    private String reason;

}