package com.coocaa.notice.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coocaa.core.log.model.LogApi;
import com.coocaa.notice.entity.Mail;
import com.coocaa.notice.entity.MsgLog;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;

import java.util.*;

/**
 * @author 陈煜坤
 * @date 2019/7/23  8:46
 * @package_name com.monitoring.warn_notice.service
 */
public interface IMailService extends IService<MsgLog> {


    boolean addMailNoticeToMQ(Mail mail);

    void updateStatus(Long msgId, Integer status);

    MsgLog selectByMsgId(Long msgId);

    List<MsgLog> selectTimeoutMsg();

    void updateTryCount(Long msgId);
}
