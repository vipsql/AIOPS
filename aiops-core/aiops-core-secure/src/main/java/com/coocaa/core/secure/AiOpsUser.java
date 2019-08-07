package com.coocaa.core.secure;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;


/**
 * 用户实体
 *
 * @author dongyang_wu
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiOpsUser {

    /**
     * 用户id
     */
    @ApiModelProperty(hidden = true)
    private Long userId;
    /**
     * 昵称
     */
    @ApiModelProperty(hidden = true)
    private String userName;
    /**
     * 账号
     */
    @ApiModelProperty(hidden = true)
    private String account;
    /**
     * 角色id
     */
    @ApiModelProperty(hidden = true)
    private String roleId;
    /**
     * 角色名
     */
    @ApiModelProperty(hidden = true)
    private String roleName;

}
