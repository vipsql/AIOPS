package com.coocaa.notice.mq.listener;

import com.coocaa.notice.config.RabbitConfig;
import com.coocaa.notice.mq.BaseConsumerProxy;
import com.coocaa.notice.mq.consumer.BaseConsumer;
import com.coocaa.notice.mq.consumer.MailConsumer;
import com.coocaa.notice.service.IMailService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MailListener {

    @Autowired
    private MailConsumer mailConsumer;

    @Autowired
    private IMailService msgLogService;

    @RabbitListener(queues = RabbitConfig.MAIL_QUEUE_NAME)
    public void consume(Message message, Channel channel) throws IOException {
        BaseConsumerProxy baseConsumerProxy = new BaseConsumerProxy(mailConsumer, msgLogService);
        BaseConsumer proxy = (BaseConsumer) baseConsumerProxy.getProxy();
        if (null != proxy) {
            proxy.consume(message, channel);
        }
    }

}
