package com.coocaa.detector.feign;

import com.coocaa.core.secure.constant.AppConstant;
import com.coocaa.core.tool.api.R;
import com.coocaa.detector.entity.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @program: intelligent_maintenance
 * @description: 内部调用
 * @author: dongyang_wu
 * @create: 2019-08-02 10:52
 */
@FeignClient(
        value = AppConstant.APPLICATION_DETECTOR_NAME
)
public interface IDetectorFeign {
    String API_PREFIX = "/detector";

    @PostMapping(API_PREFIX + "/detect")
    R<DetectorResult> timeSeriesDetector(@RequestBody Detector detector);
}