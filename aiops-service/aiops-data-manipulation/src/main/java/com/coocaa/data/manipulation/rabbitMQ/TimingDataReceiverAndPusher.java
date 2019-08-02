package com.monitoring.data_manipulation.rabbitMQ;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * @Auther: wyx
 * @Date: 2019-07-25 11:42
 * @Description: 接收定时数据并将数据推送给前端
 */
@Component
@RabbitListener(queues = "timingData")
public class TimingDataReceiverAndPusher {

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @RabbitHandler
    public void receiveAndPush(String message) {
        System.out.println("receiver: ");
        System.out.println(message);
        simpMessagingTemplate.convertAndSend("/monitoring/timingData", message);
    }

}
