package com.coocaa.data.manipulation.entity;

import lombok.*;

/**
 * @program: intelligent_maintenance
 * @description: 定时任务出错信息
 * @author: dongyang_wu
 * @create: 2019-08-01 16:21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskError {

    private Integer taskId;
    /**
     * 堆栈信息
     */
    private String stackTrace;
    /**
     * 异常名
     */
    private String exceptionName;
    /**
     * 异常消息
     */
    private String message;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 代码行数
     */
    private Integer lineNumber;
}