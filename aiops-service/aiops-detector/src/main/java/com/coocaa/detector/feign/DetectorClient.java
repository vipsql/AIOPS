package com.coocaa.detector.feign;

import com.alibaba.fastjson.JSON;
import com.coocaa.core.tool.api.R;
import com.coocaa.detector.entity.*;
import com.coocaa.detector.servcie.MetisService;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.annotations.ApiIgnore;

import java.util.*;

/**
 * @description: Feign实现类
 * @author: dongyang_wu
 * @create: 2019-08-02 10:54
 */
@RestController
@ApiIgnore
public class DetectorClient implements IDetectorClient {
    @Autowired
    private MetisService metisService;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${metis.start.bootrap}")
    public String HTTPPATH;

    @Override
    public R<DetectorResult> timeSeriesDetector(Detector detector) {
        DetectorResult detectorResult = metisService.detection(detector);
        if (detectorResult == null)
            return R.fail();
        return R.data(detectorResult);
    }

    @Override
    public R<Boolean> train(Train train) {
        boolean b = metisService.train(train);
        if (b) {
            return R.success("模型成功提交训练", true);
        } else {
            return R.fail(500, "提交模型训练失败,请检测所所选范围内是否有数据，或者是否都拥有正负样本数据", false);
        }
    }

    @Override
    public R<String> importSample(Map<String, Object> metrics) {
        restTemplate.postForObject(HTTPPATH + "/ImportSample", JSON.toJSONString(metrics), String.class);
        return R.success();
    }

    @Override
    public R<String[]> getModelSource() {
        String[] allModelSource = metisService.getAllModelSource();
        return R.data(allModelSource);
    }

    @Override
    public R<String[]> getModelSource(String sourceName) {
        String[] allModelSource = metisService.getModelSource(sourceName);
        return R.data(allModelSource);
    }

    @Override
    public R<String[]> getQueryTrainSource() {
        String[] allQueryTrainSource = metisService.getAllQueryTrainSource();
        return R.data(allQueryTrainSource);
    }

    @Override
    public R<List<String>> getQueryTrainSource(String sourceName) {
        String[] allQueryTrainSource = metisService.getAllQueryTrainSource();
        List<String> list = new ArrayList<>();
        for (String s : allQueryTrainSource) {
            if (s.contains(sourceName)) {
                list.add(s);
            }
        }
        return R.data(list);
    }

    @Override
    public R<String> getModelStatus(String modelName) {
        String modelStatus = metisService.getModelStatus(modelName);
        return R.data(modelStatus);
    }
}