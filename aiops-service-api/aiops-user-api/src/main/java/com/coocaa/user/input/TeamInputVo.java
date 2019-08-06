package com.coocaa.user.input;

import lombok.*;

/**
 * @description: Team输入实体类
 * @author: dongyang_wu
 * @create: 2019-08-05 19:43
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TeamInputVo {
    private Long id;
    private String name;
    private Long adminUserId;
    /**
     * userIdList集合以空格隔开
     */
    private String userIdList;
}