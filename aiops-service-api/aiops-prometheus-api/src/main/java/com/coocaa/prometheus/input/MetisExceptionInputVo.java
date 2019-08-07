package com.coocaa.prometheus.input;

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
public class MetisExceptionInputVo {
    private Long id;
    private Integer status;
    private String reason;

}