package com.coocaa.core.log.publisher;


import com.coocaa.core.log.event.ApiLogEvent;
import com.coocaa.core.log.model.LogApi;
import com.coocaa.core.log.annotation.ApiLog;
import com.coocaa.core.log.constant.EventConstant;
import com.coocaa.core.log.utils.LogAbstractUtil;
import com.coocaa.core.tool.constant.AiOpsConstant;
import com.coocaa.core.tool.utils.SpringUtil;
import com.coocaa.core.tool.utils.WebUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * API日志信息事件发送
 *
 * @author dongyang_wu
 */
public class ApiLogPublisher {

	public static void publishEvent(String methodName, String methodClass, ApiLog apiLog, long time) {
		HttpServletRequest request = WebUtil.getRequest();
		LogApi logApi = new LogApi();
		logApi.setType(AiOpsConstant.LOG_NORMAL_TYPE);
		logApi.setTitle(apiLog.value());
		logApi.setTime(String.valueOf(time));
		logApi.setMethodClass(methodClass);
		logApi.setMethodName(methodName);

		LogAbstractUtil.addRequestInfoToLog(request, logApi);
		Map<String, Object> event = new HashMap<>(16);
		event.put(EventConstant.EVENT_LOG, logApi);
		SpringUtil.publishEvent(new ApiLogEvent(event));
	}

}
