package com.coocaa.notice.task;

import com.coocaa.common.constant.Constant;
import com.coocaa.core.tool.jackson.JsonUtil;
import com.coocaa.notice.entity.Mail;
import com.coocaa.notice.util.MessageHelper;
import com.coocaa.notice.entity.MsgLog;
import com.coocaa.notice.service.IMailService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class ResendMsg {

    @Autowired
    private IMailService msgLogService;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 最大投递次数
    private static final int MAX_TRY_COUNT = 3;

    /**
     * 每30s拉取投递失败的消息, 重新投递
     */
//    @Scheduled(cron = "0/59 * * * * ?")
    public void resend() {
        log.info("开始执行定时任务(重新投递消息)");
        List<MsgLog> msgLogs = msgLogService.selectTimeoutMsg();
        msgLogs.forEach(msgLog -> {
            Long msgId = msgLog.getId();
            if (msgLog.getTryCount() >= MAX_TRY_COUNT) {
                msgLogService.updateStatus(msgId, Constant.MsgLogStatus.DELIVER_FAIL);
                log.info("超过最大重试次数, 消息投递失败, msgId: {}", msgId);
            } else {
                // 投递次数+1
                msgLogService.updateTryCount(msgId);
                CorrelationData correlationData = new CorrelationData(msgId.toString());
                // 去除转义字符\
                String msg =StringEscapeUtils.unescapeJava(msgLog.getMsg());
                // 去除字符串两边的""
                Mail mail = JsonUtil.parse(msg.substring(1, msg.length() - 1), Mail.class);
                mail.setMsgId(msgId.toString());
                // 重新投递
                rabbitTemplate.convertAndSend(msgLog.getExchange(), msgLog.getRoutingKey(), MessageHelper.objToMsg(mail), correlationData);
                log.info("第 " + (msgLog.getTryCount() + 1) + " 次重新投递消息");
            }
        });
        log.info("定时任务执行结束(重新投递消息)");
    }

}
