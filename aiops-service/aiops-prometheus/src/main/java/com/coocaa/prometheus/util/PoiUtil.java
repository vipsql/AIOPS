package com.coocaa.prometheus.util;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @description: CSV导出导入工具类
 * @author: dongyang_wu
 * @create: 2019-08-06 15:21
 */
public class PoiUtil {
    /**
     * CSV文件列分隔符
     */
    private static final String CSV_COLUMN_SEPARATOR = ",";

    /**
     * CSV文件行分隔符
     */
    private static final String CSV_RN = "\r\n";

    public static void exportData2Csv(List dataList, List columns, List header, OutputStream os) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < header.size(); i++) {
            buf.append(header.get(i).toString()).append(CSV_COLUMN_SEPARATOR);
        }
        buf.append(CSV_RN);
        try {
            // 设置数据
            for (int i = 0; i < dataList.size(); i++) {
                Object data = dataList.get(i);
                for (int j = 0; j < columns.size(); j++) {
                    String column = columns.get(j).toString();
                    Field field = data.getClass().getDeclaredField(column);
                    field.setAccessible(true);
                    if (column.contains("data")) {
                        buf.append("\"" + field.get(data) + "\"").append(CSV_COLUMN_SEPARATOR);
                    } else {
                        buf.append(field.get(data) != null ? field.get(data).toString() : "").append(CSV_COLUMN_SEPARATOR);
                    }
                }
                buf.append(CSV_RN);
            }
            // 写出响应
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(buf.toString());
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void responseSetProperties(String fileName, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        String userAgent = request.getHeader("User-Agent");
        // 设置文件后缀
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        String fn = fileName + sdf.format(new Date()) + ".csv";
        // 如果没有userAgent，则默认使用IE的方式进行编码，因为毕竟IE还是占多数的
        String rtn = "filename=\"" + fn + "\"";
        if (userAgent != null) {
            userAgent = userAgent.toLowerCase();
            // IE浏览器，只能采用URLEncoder编码
            if (userAgent.indexOf("IE") != -1) {
                rtn = "filename=\"" + fn + "\"";
            }
            // Opera浏览器只能采用filename*
            else if (userAgent.indexOf("OPERA") != -1) {
                rtn = "filename*=UTF-8''" + fn;
            }
            // Safari浏览器，只能采用ISO编码的中文输出
            else if (userAgent.indexOf("SAFARI") != -1) {
                rtn = "filename=\"" + new String(fn.getBytes("UTF-8"), "ISO8859-1")
                        + "\"";
            }
            // FireFox浏览器，可以使用MimeUtility或filename*或ISO编码的中文输出
            else if (userAgent.indexOf("FIREFOX") != -1) {
                rtn = "filename*=UTF-8''" + fn;
            }
        }
        String headStr = "attachment;  " + rtn;
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/msexcel;charset=UTF-8");
        response.setHeader("Content-Disposition", headStr);
    }
}