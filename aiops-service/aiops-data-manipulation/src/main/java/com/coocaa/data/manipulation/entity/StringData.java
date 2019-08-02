package com.monitoring.data_manipulation.entity;

import lombok.*;

import java.util.List;

/**
 * @Auther: wyx
 * @Date: 2019-07-27 14:21
 * @Description: 字符串数据
 */
@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StringData {

    /**
     * [ <unix_time>, "<string_value>" ]
     */
    private List<String> value;

}
