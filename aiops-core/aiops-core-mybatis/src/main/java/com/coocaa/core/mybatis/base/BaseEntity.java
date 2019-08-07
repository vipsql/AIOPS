package com.coocaa.core.mybatis.base;


import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.coocaa.core.tool.utils.DateUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @description: 基础实体类
 * @author: dongyang_wu
 * @create: 2019-07-28 22:09
 */
@Data
public class BaseEntity implements Serializable {

	/**
	 * 创建人
	 */
	@ApiModelProperty(value = "创建人")
	private Integer createUser;

	/**
	 * 创建时间
	 */
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 更新人
	 */
	@ApiModelProperty(value = "更新人")
	private Integer updateUser;

	/**
	 * 更新时间
	 */
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATETIME)
	@JsonFormat(pattern = DateUtil.PATTERN_DATETIME)
	@ApiModelProperty(value = "更新时间")
	private LocalDateTime updateTime;

	/**
	 * 状态[1:正常]
	 */
	@ApiModelProperty(value = "业务状态")
	private Integer status;

	/**
	 * 状态[0:未删除,1:删除]
	 */
	@TableLogic
	@ApiModelProperty(value = "是否已删除")
	private Integer isDeleted;
}
