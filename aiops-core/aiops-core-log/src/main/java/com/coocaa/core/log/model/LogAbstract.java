package com.coocaa.core.log.model;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.coocaa.core.tool.utils.DateUtil;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * logApi、logError、logUsual的父类，拥有相同的属性值
 *
 * @author dongynag_wu
 */
@Data
public class LogAbstract implements Serializable {

    protected static final long serialVersionUID = 1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.ID_WORKER)
    protected Long id;

    /**
     * 服务ID
     */
    protected String serviceId;
    /**
     * 服务器 ip
     */
    protected String serverIp;
    /**
     * 服务器名
     */
    protected String serverHost;
    /**
     * 环境
     */
    protected String env;
    /**
     * 操作IP地址
     */
    protected String remoteIp;
    /**
     * 用户代理
     */
    protected String userAgent;
    /**
     * 请求URI
     */
    protected String requestUri;
    /**
     * 操作方式
     */
    protected String method;
    /**
     * 方法类
     */
    protected String methodClass;
    /**
     * 方法名
     */
    protected String methodName;
    /**
     * 操作提交的数据
     */
    protected String params;
    /**
     * 执行时间
     */
    protected String time;

    /**
     * 创建人
     */
    protected String createBy;

    /**
     * 创建时间
     */
    protected Date createTime;

}
