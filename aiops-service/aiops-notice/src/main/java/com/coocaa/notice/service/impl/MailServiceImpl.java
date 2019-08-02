package com.coocaa.notice.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coocaa.core.tool.utils.*;
import com.coocaa.notice.util.MessageHelper;
import com.coocaa.notice.config.RabbitConfig;
import com.coocaa.notice.entity.Mail;
import com.coocaa.notice.entity.MsgLog;
import com.coocaa.notice.mapper.MsgLogMapper;
import com.coocaa.notice.service.IMailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @author 陈煜坤
 * @date 2019/7/23  8:47
 * @package_name com.monitoring.warn_notice.service
 */
@Service("MailService")
public class MailServiceImpl extends ServiceImpl<MsgLogMapper, MsgLog> implements IMailService {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    JavaMailSenderImpl mailSender;


    @Override
    public boolean addMailNoticeToMQ(Mail mail) {
        MsgLog msgLog = MsgLog.builder().createTime(DateUtil.currentDate()).exchange(RabbitConfig.MAIL_EXCHANGE_NAME).routingKey(RabbitConfig.MAIL_ROUTING_KEY_NAME).build();
        msgLog.setMsg(mail);
        msgLog.setNextTryTime(msgLog.getCreateTime());
        boolean insert = msgLog.insert();
        if (insert) {
            mail.setMsgId(msgLog.getId().toString());
            // 发送消息
            CorrelationData correlationData = new CorrelationData(msgLog.getId().toString());
            rabbitTemplate.convertAndSend(RabbitConfig.MAIL_EXCHANGE_NAME, RabbitConfig.MAIL_ROUTING_KEY_NAME, MessageHelper.objToMsg(mail), correlationData);
            return true;
        }
        return false;
    }

    @Autowired
    private MsgLogMapper msgLogMapper;

    @Override
    public void updateStatus(Long msgId, Integer status) {
        MsgLog msgLog = new MsgLog();
        msgLog.setId(msgId);
        msgLog.setStatus(status);
        msgLog.setUpdateTime(new Date());
        msgLogMapper.updateStatus(msgLog);
    }

    @Override
    public MsgLog selectByMsgId(Long msgId) {
        return msgLogMapper.selectByPrimaryKey(msgId);
    }

    @Override
    public List<MsgLog> selectTimeoutMsg() {
        return msgLogMapper.selectTimeoutMsg();
    }

    @Override
    public void updateTryCount(Long msgId) {
        Date nextTryTime = DateUtil.currentDate();
        MsgLog msgLog = new MsgLog();
        msgLog.setId(msgId);
        msgLog.setNextTryTime(nextTryTime);
        msgLogMapper.updateTryCount(msgLog);
    }
}
