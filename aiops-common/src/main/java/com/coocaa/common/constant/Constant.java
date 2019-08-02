package com.coocaa.common.constant;

public class Constant {


    public interface LogType {
        Integer LOGIN = 1;// 登录
        Integer LOGOUT = 2;// 登出
    }

    public interface MsgLogStatus {
        Integer DELIVERING = 0;// 消息投递中
        Integer DELIVER_SUCCESS = 1;// 投递成功
        Integer DELIVER_FAIL = 2;// 投递失败
        Integer CONSUMED_SUCCESS = 3;// 已消费
    }

    public interface NumberType {
        Byte BAD_PROPERTY = 0;
        Byte GOOD_PROPERTY = 1;
        Integer ZERO_PROPERTY = 0;
        Integer ONE_PROPERTY = 1;
        Integer TWO_PROPERTY = 2;
        Integer THREE_PROPERTY = 3;
        Integer FOUR_PROPERTY = 4;
        Integer FIVE_PROPERTY = 5;
        Integer SIX_PROPERTY = 6;
        Integer SEVEN_PROPERTY = 7;
        Integer EIGHT_PROPERTY = 8;
    }
}
