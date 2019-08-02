package com.coocaa.core.secure.interceptor;

import com.coocaa.core.secure.AiOpsUser;
import com.coocaa.core.secure.utils.SecureUtil;
import com.coocaa.core.tool.api.R;
import com.coocaa.core.tool.api.ResultCode;
import com.coocaa.core.tool.constant.AiOpsConstant;
import com.coocaa.core.tool.jackson.JsonUtil;
import com.coocaa.core.tool.utils.StringUtil;
import com.coocaa.core.tool.utils.WebUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/**
 * 客户端校验
 *
 * @author Chill
 */
@Slf4j
@AllArgsConstructor
public class ClientInterceptor extends HandlerInterceptorAdapter {

	private final String clientId;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		AiOpsUser user = SecureUtil.getUser();
		log.info(clientId);
		if (user != null && StringUtil.equals(clientId, SecureUtil.getClientIdFromHeader()) && StringUtil.equals(clientId, user.getClientId())) {
			return true;
		} else {
			log.warn("客户端认证失败---，请求接口：{}，请求IP：{}，请求参数：{}", request.getRequestURI(), WebUtil.getIP(request), JsonUtil.toJson(request.getParameterMap()));
			R result = R.fail(ResultCode.UN_AUTHORIZED);
			response.setHeader(AiOpsConstant.CONTENT_TYPE_NAME, MediaType.APPLICATION_JSON_UTF8_VALUE);
			response.setCharacterEncoding(AiOpsConstant.UTF_8);
			response.setStatus(HttpServletResponse.SC_OK);
			try {
				response.getWriter().write(Objects.requireNonNull(JsonUtil.toJson(result)));
			} catch (IOException ex) {
				log.error(ex.getMessage());
			}
			return false;
		}
	}

}
