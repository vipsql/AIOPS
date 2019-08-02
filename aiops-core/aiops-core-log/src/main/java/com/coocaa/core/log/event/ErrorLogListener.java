/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.coocaa.core.log.event;


import com.coocaa.core.log.feign.ILogClient;
import com.coocaa.core.log.props.BladeProperties;
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
 * @author Chill
 */
@Slf4j
@AllArgsConstructor
public class ErrorLogListener {

	private final ILogClient logService;
	private final ServerInfo serverInfo;
	private final BladeProperties bladeProperties;

	@Async
	@Order
	@EventListener(ErrorLogEvent.class)
	public void saveErrorLog(ErrorLogEvent event) {
		Map<String, Object> source = (Map<String, Object>) event.getSource();
		LogError logError = (LogError) source.get(EventConstant.EVENT_LOG);
		LogAbstractUtil.addOtherInfoToLog(logError, bladeProperties, serverInfo);
		logService.saveErrorLog(logError);
	}

}
