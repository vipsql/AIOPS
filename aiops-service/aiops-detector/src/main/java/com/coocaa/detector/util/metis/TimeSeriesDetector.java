package com.coocaa.detector.util.metis;

import com.alibaba.fastjson.JSON;
import com.coocaa.core.tool.response.WarnStatusEnum;
import com.coocaa.detector.entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author 陈煜坤
 * @date 2019/7/24  12:52
 * @package_name com.monitoring.data_manipulation.utils.metis
 */
@Component
public class TimeSeriesDetector {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${metis.start.bootrap}")
    public String HTTPPATH;

    /**
     * 有模型检测的方法
     *
     * @param detector 模型数据类
     * @return 返回一个警告状态水平参数类
     */
    public DetectorResult detect(Detector detector) {
        try {
            if (detector.isSomeEmtry()) {
                String request = JSON.toJSONString(detector);
                String result = restTemplate.postForObject(HTTPPATH + "/PredictValue", request, String.class);
                System.out.println(result);
                DetectorResult detectorResult = JSON.parseObject(result, DetectorResult.class);
                System.out.println(detectorResult);
                return detectorResult;
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
}