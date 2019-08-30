package com.coocaa.core.tool.singleton;

import com.coocaa.core.tool.utils.IpUtil;
import com.coocaa.core.tool.utils.SpringUtil;
import lombok.Getter;

/**
 * @author: dongyang_wu
 * @create: 2019-08-29 10:23
 * @description: 枚举单例
 */
public enum SingleTonContextEnum {
    INSTANCE;
    @Getter
    private IpUtil ipUtil;

    SingleTonContextEnum() {
        ipUtil = SpringUtil.getBean(IpUtil.class);
    }
}