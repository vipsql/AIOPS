package com.coocaa.detector.servcie.impl;

import com.coocaa.detector.entity.*;
import com.coocaa.detector.servcie.MetisService;
import com.coocaa.detector.util.metis.TimeSeriesDetector;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 陈煜坤
 * @date 2019/7/30  15:01
 * @package_name com.monitoring.warn.notice.service.impl
 */
@Service
@AllArgsConstructor
public class MetisServiceImpl implements MetisService {

    private TimeSeriesDetector timeSeriesDetector;

    @Override
    public DetectorResult detection(Detector detector) {
        DetectorResult detect = timeSeriesDetector.detect(detector);
        return detect;
    }
}