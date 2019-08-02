package com.coocaa.common.entity;

import lombok.Data;

/**
 * @author 陈煜坤
 * @date 2019/7/29  20:34
 * @package_name com.common.parent_model.entity
 */
@Data
public class NoticeMail {

    private String to;
    private String from;
    private String subject;
    private String text;
}
