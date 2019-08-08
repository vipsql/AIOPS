package com.coocaa.detector.controller;

import com.alibaba.fastjson.JSON;
import com.coocaa.core.log.response.ResponseHelper;
import com.coocaa.core.log.response.ResultBean;
import com.coocaa.core.tool.response.ReturnData;
import com.coocaa.detector.entity.*;
import com.coocaa.detector.servcie.MetisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @author 陈煜坤
 * @date 2019/7/30  14:59
 * @package_name com.monitoring.warn.notice.controller
 */
@RestController
@Api(value = "异常检测模块", tags = "异常检测接口")
public class MetisController {
    @Autowired
    private MetisService metisService;
    @Value("${metis.start.bootrap}")
    public String HTTPPATH;
    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/metis/detector")
    @ApiOperation(value = "异常检测")
    public ResponseEntity<ResultBean> timeSeriesDetector(@RequestBody Detector detector) {
        DetectorResult detectorResult = metisService.detection(detector);
        return ResponseHelper.OK(detectorResult);
    }

    @PostMapping("/Train")
    public ReturnData train(Train train) {
        boolean b = metisService.train(train);
        if (b) {
            return ReturnData.success("模型成功提交训练");
        } else {
            return ReturnData.fail(500, "提交模型训练失败,请检测所所选范围内是否有数据，或者是否都拥有正负样本数据");
        }
    }

    @PostMapping("/ImportSample")
    public ReturnData ImportSample(@RequestBody Map<String, Object> metrics) {
        System.out.println(metrics);
        restTemplate.postForObject(HTTPPATH + "/ImportSample", JSON.toJSONString(metrics), String.class);
        return ReturnData.success();
    }

    @GetMapping("/ModelSource")
    public ReturnData getModelSource() {
        String[] allModelSource = metisService.getAllModelSource();
        return ReturnData.success(allModelSource);
    }

    @GetMapping("/ModelSource/{sourceName}")
    public ReturnData getModelSource(@PathVariable("sourceName") String sourceName) {
        String[] allModelSource = metisService.getModelSource(sourceName);
        return ReturnData.success(allModelSource);
    }

    @GetMapping("/QueryTrainSource")
    public ReturnData getQueryTrainSource() {
        String[] allQueryTrainSource = metisService.getAllQueryTrainSource();
        return ReturnData.success(allQueryTrainSource);
    }

    @GetMapping("/QueryTrainSource/{sourceName}")
    public ReturnData getQueryTrainSource(@PathVariable("sourceName") String sourceName) {
        String[] allQueryTrainSource = metisService.getAllQueryTrainSource();
        List<String> list = new ArrayList<>();
        for (String s : allQueryTrainSource) {
            if (s.contains(sourceName)) {
                list.add(s);
            }
        }
        return ReturnData.success(list);
    }

    @GetMapping("/ModelStatus/{modelName}")
    public ReturnData getModelStatus(@PathVariable("modelName") String modelName) {
        String modelStatus = metisService.getModelStatus(modelName);
        return ReturnData.success(modelStatus);
    }
}