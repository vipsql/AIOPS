package com.coocaa.notice.mq;

import com.coocaa.common.constant.Constant;
import com.coocaa.notice.entity.Mail;
import com.coocaa.notice.entity.MsgLog;
import com.coocaa.notice.service.IMailService;
import com.coocaa.notice.util.MessageHelper;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.util.StringUtils;

import java.lang.reflect.Proxy;

@Slf4j
public class BaseConsumerProxy {

    private Object target;

    private IMailService msgLogService;

    public BaseConsumerProxy(Object target, IMailService msgLogService) {
        this.target = target;
        this.msgLogService = msgLogService;
    }

    public Object getProxy() {
        ClassLoader classLoader = target.getClass().getClassLoader();
        Class[] interfaces = target.getClass().getInterfaces();

        Object proxy = Proxy.newProxyInstance(classLoader, interfaces, (proxy1, method, args) -> {
            Message message = (Message) args[0];
            Channel channel = (Channel) args[1];
            String correlationId = getCorrelationId(message);
            // 消费幂等性, 防止消息被重复消费
            if (StringUtils.isEmpty(correlationId) || isConsumed(correlationId)) {
                log.info("重复消费, correlationId: {}", correlationId);
                return null;
            }

            MessageProperties properties = message.getMessageProperties();
            long tag = properties.getDeliveryTag();

            try {
                // 真正消费的业务逻辑
                Object result = method.invoke(target, args);
                msgLogService.updateStatus(Long.valueOf(correlationId), Constant.MsgLogStatus.CONSUMED_SUCCESS);
                // 消费确认
                channel.basicAck(tag, false);
                return result;
            } catch (Exception e) {
                log.error("getProxy error", e);
                channel.basicNack(tag, false, true);
                return null;
            }
        });

        return proxy;
    }

    /**
     * 获取CorrelationId
     *
     * @param message
     * @return
     */
    private String getCorrelationId(Message message) {
        Mail mail = MessageHelper.msgToObj(message, Mail.class);
        return mail.getMsgId();
    }

    /**
     * 消息是否已被消费
     *
     * @param correlationId
     * @return
     */
    private boolean isConsumed(String correlationId) {
        MsgLog msgLog = msgLogService.selectByMsgId(Long.valueOf(correlationId));
        System.out.println(msgLog);
        if (null == msgLog || msgLog.getStatus().equals(Constant.MsgLogStatus.CONSUMED_SUCCESS)) {
            return true;
        }

        return false;
    }

}
