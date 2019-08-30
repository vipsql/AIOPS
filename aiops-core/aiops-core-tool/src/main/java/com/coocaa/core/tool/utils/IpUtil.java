package com.coocaa.core.tool.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.stereotype.Component;

import java.net.*;
import java.util.Enumeration;

/**
 * @author: dongyang_wu
 * @create: 2019-08-29 10:19
 * @description: Ip工具类
 */
@Component
public class IpUtil {
    @Value("${server.port}")
    private String port;

    public String getCurrentInstance() {
        try {
            return Inet4Address.getLocalHost().getHostAddress() + ":" + port;
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return "不支持的主机Ip";
    }

    /**
     * 获得主机IP
     *
     * @return String
     */
    public boolean isWindowsOS() {
        boolean isWindowsOS = false;
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().contains("windows")) {
            isWindowsOS = true;
        }
        return isWindowsOS;
    }

    /**
     * 获取本机ip地址，并自动区分Windows还是linux操作系统
     *
     * @return String
     */
    public String getLocalIP() {
        String sIP = "";
        InetAddress ip = null;
        try {
            //如果是Windows操作系统
            if (isWindowsOS()) {
                ip = InetAddress.getLocalHost();
            }
            //如果是Linux操作系统
            else {
                boolean bFindIP = false;
                Enumeration<NetworkInterface> netInterfaces = (Enumeration<NetworkInterface>) NetworkInterface
                        .getNetworkInterfaces();
                while (netInterfaces.hasMoreElements()) {
                    if (bFindIP) {
                        break;
                    }
                    NetworkInterface ni = (NetworkInterface) netInterfaces.nextElement();
                    //----------特定情况，可以考虑用ni.getName判断
                    //遍历所有ip
                    Enumeration<InetAddress> ips = ni.getInetAddresses();
                    while (ips.hasMoreElements()) {
                        ip = (InetAddress) ips.nextElement();
                        if (ip.isSiteLocalAddress()
                                && !ip.isLoopbackAddress()   //127.开头的都是lookback地址
                                && ip.getHostAddress().indexOf(":") == -1) {
                            bFindIP = true;
                            break;
                        }
                    }

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (null != ip) {
            sIP = ip.getHostAddress();
        }
        return sIP + ":" + port;
    }

}