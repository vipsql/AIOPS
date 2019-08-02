package com.coocaa.detector.feign;

import com.coocaa.core.tool.api.R;
import com.coocaa.detector.entity.*;
import com.coocaa.detector.servcie.MetisService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: intelligent_maintenance
 * @description: Feign实现类
 * @author: dongyang_wu
 * @create: 2019-08-02 10:54
 */
@RestController
@AllArgsConstructor
public class DetectorFeign implements IDetectorFeign {
    private MetisService metisService;

    @Override
    public R<DetectorResult> timeSeriesDetector(Detector detector) {
        DetectorResult detectorResult = metisService.detection(detector);
        if (detectorResult == null)
            return R.fail();
        return R.data(detectorResult);
    }
}