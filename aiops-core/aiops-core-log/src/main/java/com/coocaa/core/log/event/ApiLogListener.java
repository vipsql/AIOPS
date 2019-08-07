package com.coocaa.core.log.event;

import com.coocaa.core.log.feign.ILogClient;
import com.coocaa.core.log.props.AiopsProperties;
import com.coocaa.core.log.server.ServerInfo;
import com.coocaa.core.log.constant.EventConstant;
import com.coocaa.core.log.model.LogApi;
import com.coocaa.core.log.utils.LogAbstractUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

import java.util.Map;


/**
 * 异步监听日志事件
 *
 * @author dongyang_wu
 */
@Slf4j
@AllArgsConstructor
public class ApiLogListener {

	private final ILogClient logService;
	private final ServerInfo serverInfo;
	private final AiopsProperties aiopsProperties;


	@Async
	@Order
	@EventListener(ApiLogEvent.class)
	public void saveApiLog(ApiLogEvent event) {
		Map<String, Object> source = (Map<String, Object>) event.getSource();
		LogApi logApi = (LogApi) source.get(EventConstant.EVENT_LOG);
		LogAbstractUtil.addOtherInfoToLog(logApi, aiopsProperties, serverInfo);
		logService.saveApiLog(logApi);
	}

}
