package com.coocaa.common.constant;

import java.util.Arrays;
import java.util.List;

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

    public interface MetisCsv {
        List<String> columns = Arrays.asList("viewName", "viewId", "attrName", "attrId", "source", "trainOrTest", "positiveOrNegative", "window", "dataC", "dataB", "dataA", "dateTime");
        List<String> columnCns = Arrays.asList("指标集名称", "指标集id", "指标名称", "指标id", "数据来源", "训练集/测试集", "正样本/负样本", "样本窗口", "dataC", "dataB", "dataA", "数据时间戳");
    }

    public interface MetisException {
        Integer RET = 0;
        Double P = 0.15;
    }

    // 0未处理1已修正2已恢复3已修复
    public interface MetisExceptionStatus {
        Integer UNHANDLED = 0;// 未处理
        Integer CORRECTED = 1;// 已修正
        Integer RECOVERY = 2;// 已恢复
        Integer HANDLED = 3;// 已修复
    }
}
