package com.coocaa.core.log.utils;

import com.coocaa.core.log.model.LogAbstract;
import com.coocaa.core.log.props.AiopsProperties;
import com.coocaa.core.log.server.ServerInfo;
import com.coocaa.core.secure.utils.SecureUtil;
import com.coocaa.core.tool.utils.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * INet 相关工具
 *
 * @author dongyang_wu
 */
public class LogAbstractUtil {

    /**
     * 向log中添加补齐request的信息
     *
     * @param request     请求
     * @param logAbstract 日志基础类
     */
    public static void addRequestInfoToLog(HttpServletRequest request, LogAbstract logAbstract) {
        logAbstract.setRemoteIp(WebUtil.getIP(request));
        logAbstract.setUserAgent(request.getHeader(WebUtil.USER_AGENT_HEADER));
        logAbstract.setRequestUri(UrlUtil.getPath(request.getRequestURI()));
        logAbstract.setMethod(request.getMethod());
        logAbstract.setParams(WebUtil.getRequestParamString(request));
        logAbstract.setCreateBy(SecureUtil.getUserAccount(request));
    }

    /**
     * 向log中添加补齐其他的信息（eg：blade、server等）
     *
     * @param logAbstract     日志基础类
     * @param aiopsProperties 配置信息
     * @param serverInfo      服务信息
     */
    public static void addOtherInfoToLog(LogAbstract logAbstract, AiopsProperties aiopsProperties, ServerInfo serverInfo) {
        logAbstract.setServiceId(aiopsProperties.getName());
        logAbstract.setServerHost(serverInfo.getHostName());
        logAbstract.setServerIp(serverInfo.getIpWithPort());
        logAbstract.setEnv(aiopsProperties.getEnv());
        logAbstract.setCreateTime(new Date());

        //这里判断一下params为null的情况，否则blade-log服务在解析该字段的时候，可能会报出NPE
        if (logAbstract.getParams() == null) {
            logAbstract.setParams(StringPool.EMPTY);
        }
    }
}
