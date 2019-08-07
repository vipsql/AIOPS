package com.coocaa.core.log.event;
import com.coocaa.core.log.feign.ILogClient;
import com.coocaa.core.log.props.AiopsProperties;
import com.coocaa.core.log.server.ServerInfo;
import com.coocaa.core.log.constant.EventConstant;
import com.coocaa.core.log.model.LogError;
import com.coocaa.core.log.utils.LogAbstractUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

import java.util.Map;

/**
 * 异步监听错误日志事件
 *
 * @author dongyang_wu
 */
@Slf4j
@AllArgsConstructor
public class ErrorLogListener {

	private final ILogClient logService;
	private final ServerInfo serverInfo;
	private final AiopsProperties aiopsProperties;

	@Async
	@Order
	@EventListener(ErrorLogEvent.class)
	public void saveErrorLog(ErrorLogEvent event) {
		Map<String, Object> source = (Map<String, Object>) event.getSource();
		LogError logError = (LogError) source.get(EventConstant.EVENT_LOG);
		LogAbstractUtil.addOtherInfoToLog(logError, aiopsProperties, serverInfo);
		logService.saveErrorLog(logError);
	}

}
