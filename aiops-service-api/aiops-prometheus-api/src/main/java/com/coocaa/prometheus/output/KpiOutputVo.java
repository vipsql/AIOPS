package com.coocaa.prometheus.output;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import lombok.*;

import java.util.Date;

/**
 * @author: dongyang_wu
 * @create: 2019-08-19 10:23
 * @description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KpiOutputVo {
    private Long id;
    private String name;
    private String promExpression;
    private String queryPromExpression;
    private Date updateTime;
    private Date createTime;
}