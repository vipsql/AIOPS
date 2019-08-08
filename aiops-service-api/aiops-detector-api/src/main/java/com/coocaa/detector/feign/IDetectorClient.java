package com.coocaa.detector.feign;

import com.coocaa.core.secure.constant.AppConstant;
import com.coocaa.core.tool.api.R;
import com.coocaa.detector.entity.*;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @description: 内部调用
 * @author: dongyang_wu
 * @create: 2019-08-02 10:52
 */
@FeignClient(
        value = AppConstant.APPLICATION_DETECTOR_NAME,
        fallback = IDetectorClientFallback.class
)
public interface IDetectorClient {
    String API_PREFIX = "/detector";

    @PostMapping(API_PREFIX + "/detect")
    R<DetectorResult> timeSeriesDetector(@RequestBody Detector detector);

    @PostMapping(API_PREFIX + "/train")
    R<Boolean> train(@RequestBody Train train);

    /**
     * 异常标注后在上传
     * @param metrics
     * @return
     */
    @PostMapping(API_PREFIX + "/importSample")
    R<String> importSample(@RequestBody Map<String, Object> metrics);

    @GetMapping(API_PREFIX + "/modelSource")
    R<String[]> getModelSource();

    @GetMapping(API_PREFIX + "/modelSource/{sourceName}")
    R<String[]> getModelSource(@PathVariable("sourceName") String sourceName);

    @GetMapping(API_PREFIX + "/queryTrainSource")
    R<String[]> getQueryTrainSource();

    @GetMapping(API_PREFIX + "/queryTrainSource/{sourceName}")
    R<List<String>> getQueryTrainSource(@PathVariable("sourceName") String sourceName);

    @GetMapping(API_PREFIX + "/modelStatus/{modelName}")
    R<String> getModelStatus(@PathVariable("modelName") String modelName);

}