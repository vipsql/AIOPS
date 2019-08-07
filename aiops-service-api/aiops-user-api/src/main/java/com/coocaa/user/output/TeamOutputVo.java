package com.coocaa.user.output;

import com.coocaa.user.entity.User;
import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * @description: Team输出实体类
 * @author: dongyang_wu
 * @create: 2019-08-07 10:54
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamOutputVo {
    private Long id;
    private String name;
    private Long adminUserId;
    private Integer userListTotal;
    private List<User> userList;
    private Date createTime;
    private Date updateTime;
}