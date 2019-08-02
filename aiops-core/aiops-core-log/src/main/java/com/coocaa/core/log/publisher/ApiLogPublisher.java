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
 * @author Chill
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
