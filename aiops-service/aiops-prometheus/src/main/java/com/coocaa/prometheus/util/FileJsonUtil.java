package com.coocaa.prometheus.util;

import com.alibaba.fastjson.JSON;
import com.coocaa.common.constant.Constant;
import com.coocaa.core.log.exception.ApiException;
import com.coocaa.core.log.exception.ApiResultEnum;
import com.coocaa.prometheus.entity.PrometheusConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @program: intelligent_maintenance
 * @description: 文件Json及JavaBean转换工具类
 * @author: dongyang_wu
 * @create: 2019-07-31 22:27
 */
@Component
public class FileJsonUtil {
    @Value("${prometheus.metricsPath}")
    private String metricsPath;
    @Value("${prometheus.springbootServicePath}")
    private String springbootServicePath;
    private final String metricsPathJsonFileName = "metricsPath.json";
    private final String springbootServicePathJsonFileName = "springbootServicePath.json";

    public List<PrometheusConfig> createJsonFile(List<PrometheusConfig> prometheusConfigs, Integer type, Integer mode) {
        String fullPath = getFullPathFromType(type);
        // 读取Json文件
        String s = readJsonFile(fullPath);
        List<PrometheusConfig> preConfigs = JSON.parseArray(s, PrometheusConfig.class);
        try {
            // 取出list的所有name
            List<String> configNames = prometheusConfigs.stream()
                    .map(PrometheusConfig::getLabels)
                    .map(PrometheusConfig.LabelsBean::getInstance).collect(Collectors.toList());
            // 以lables为key切分list
            Map<PrometheusConfig.LabelsBean, PrometheusConfig> keyToConfig = prometheusConfigs.stream().collect(Collectors.toMap(PrometheusConfig::getLabels, Function.identity()));
            Boolean deleteFunctionType = Constant.NumberType.ONE_PROPERTY.equals(mode);
            // 为修改配置
            Iterator<PrometheusConfig> iterator = preConfigs.iterator();
            while (iterator.hasNext()) {
                PrometheusConfig item = iterator.next();
                if (configNames.contains(item.getLabels().getInstance())) {
                    if (!deleteFunctionType) {
                        item.setTargets(keyToConfig.get(item.getLabels()).getTargets());
                        // 将修改的从添加移除掉
                        prometheusConfigs.remove(keyToConfig.get(item.getLabels()));
                    } else {
                        // 删除操作
                        iterator.remove();
                    }
                }
            }
            // 为新增配置
            if (!deleteFunctionType)
                preConfigs.addAll(prometheusConfigs);
            File file = new File(fullPath);
            String jsonString = JSON.toJSONString(preConfigs);
            // 将格式化后的字符串写入文件
            Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            write.write(jsonString);
            write.flush();
            write.close();
        } catch (Exception e) {
            throw new ApiException(ApiResultEnum.FUNCTION_EXCEPTION.getCode(), ApiResultEnum.FUNCTION_EXCEPTION.getMessage() + e.toString());
        }
        return preConfigs;
    }

    private String getFullPathFromType(Integer type) {
        StringBuffer result = new StringBuffer();
        if (Constant.NumberType.ZERO_PROPERTY.equals(type)) {
            result.append(metricsPath).append(File.separator).append(metricsPathJsonFileName);
        } else {
            result.append(springbootServicePath).append(File.separator).append(springbootServicePathJsonFileName);
        }
        return result.toString();
    }

    private String readJsonFile(String fileName) {
        String jsonStr;
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);

            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
            int ch;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}