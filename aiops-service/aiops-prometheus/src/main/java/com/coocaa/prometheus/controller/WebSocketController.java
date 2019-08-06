package com.coocaa.prometheus.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @Auther: wyx
 * @Date: 2019-07-22 16:09
 * @Description: WebSocket 控制器
 */
@RestController
@RequestMapping("/websocket")
@ApiIgnore
public class WebSocketController {

    /**
     * 获得修改后的指标
     * 将数据发送给订阅了消息的浏览器
     * 收到消息并推送给 所有人 广播消息
     * SendToUser一对一消息
     * param: 获得的消息
     */
    @MessageMapping("/updateMetrics")
    @SendTo("/monitoring/getCurrentData")
    public void receive(JSONObject param) {
        //TODO disposal data and send data to browser
    }

}
