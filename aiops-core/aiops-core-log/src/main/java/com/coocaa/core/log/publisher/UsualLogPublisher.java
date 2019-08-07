package com.coocaa.core.log.publisher;


import com.coocaa.core.log.constant.EventConstant;
import com.coocaa.core.log.event.UsualLogEvent;
import com.coocaa.core.log.model.LogUsual;
import com.coocaa.core.log.utils.LogAbstractUtil;
import com.coocaa.core.tool.utils.SpringUtil;
import com.coocaa.core.tool.utils.WebUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 日志信息事件发送
 *
 * @author dongyang_wu
 */
public class UsualLogPublisher {

	public static void publishEvent(String level, String id, String data) {
		HttpServletRequest request = WebUtil.getRequest();
		LogUsual logUsual = new LogUsual();
		logUsual.setLogLevel(level);
		logUsual.setLogId(id);
		logUsual.setLogData(data);

		LogAbstractUtil.addRequestInfoToLog(request, logUsual);
		Map<String, Object> event = new HashMap<>(16);
		event.put(EventConstant.EVENT_LOG, logUsual);
		event.put(EventConstant.EVENT_REQUEST, request);
		SpringUtil.publishEvent(new UsualLogEvent(event));
	}

}
