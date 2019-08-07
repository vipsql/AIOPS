package com.coocaa.core.cloud.feign;

import com.coocaa.core.secure.constant.AppConstant;
import com.coocaa.core.secure.constant.TokenConstant;
import com.coocaa.core.secure.utils.SecureUtil;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * feign 传递Request header
 *
 * @author dongyang_wu
 */
@Slf4j
public class FeignRequestHeaderInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        boolean hasAuthToken = false;
        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();
            Enumeration<String> headerNames = request.getHeaderNames();
            if (headerNames != null) {
                while (headerNames.hasMoreElements()) {
                    String name = headerNames.nextElement();
                    String value = request.getHeader(name);
                    if (TokenConstant.HEADER.equalsIgnoreCase(name)) {
                        hasAuthToken = true;
                        requestTemplate.header(name, value);
                    }
                }
            }
        }
        if (!hasAuthToken) {
            Map<String, String> param = new HashMap<>(16);
            param.put(TokenConstant.USER_NAME, "Feign内部调用");
            String jwt = SecureUtil.createJWT(param, "admin", "admin", true, AppConstant.FEIGN_TOKEN_EXPIRE_TIME);
            requestTemplate.header(TokenConstant.HEADER, jwt);
        }
    }

}
