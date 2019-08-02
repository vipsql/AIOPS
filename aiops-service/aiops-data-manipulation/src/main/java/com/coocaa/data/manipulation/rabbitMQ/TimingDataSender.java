package com.monitoring.data_manipulation.rabbitMQ;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Auther: wyx
 * @Date: 2019-07-25 11:40
 * @Description: 定时数据发送
 */
@Component
public class TimingDataSender {

    @Autowired
    private AmqpTemplate rabbitTemplate;

    public void send(String message) {
        this.rabbitTemplate.convertAndSend("timingData", message);
    }

}
