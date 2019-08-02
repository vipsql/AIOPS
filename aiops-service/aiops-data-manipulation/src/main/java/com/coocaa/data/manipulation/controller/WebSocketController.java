package com.coocaa.data.manipulation.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: wyx
 * @Date: 2019-07-22 16:09
 * @Description: WebSocket 控制器
 */
@RestController
@RequestMapping("/websocket")
public class WebSocketController {

    /**
     * 获得修改后的指标
     * 将数据发送给订阅了消息的浏览器
     *
     * param: 获得的消息
     */
    @MessageMapping("/updateMetrics")
    @SendTo("/monitoring/getCurrentData")
    public void receive(JSONObject param){
        //TODO disposal data and send data to browser
    }

}
