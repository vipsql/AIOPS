package com.coocaa.prometheus.input;

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
public class MetisCsvInputVo {
    private Date begin;
    private Date end;
    /**
     * 跨度，以分钟为单位
     */
    private Integer span;
    /**
     * 对应指标Id
     */
    private Long taskId;
}