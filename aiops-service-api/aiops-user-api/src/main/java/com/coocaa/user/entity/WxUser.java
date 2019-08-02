package com.coocaa.user.entity;

import lombok.*;

/**
 * @program: 微信登录用户
 * @description:
 * @author: dongyang_wu
 * @create: 2019-05-30 23:12
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WxUser {
	/**
	 * openid
	 */
	private String openid;
	/**
	 * appid
	 */
	private String appid;
	/**
	 * 头像
	 */
	private String avatarUrl;
	/**
	 * 昵称
	 */
	private String nickName;
	/**
	 * 国家
	 */
	private String country;
	/**
	 * 省份
	 */
	private String province;
	/**
	 * 城市
	 */
	private String city;
	/**
	 * 性别
	 */
	private Byte gender;
	private String code;
	private String encryptedData;
	private String iv;
}
