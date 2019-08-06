package com.coocaa.detector.feign;

import com.coocaa.core.tool.api.R;
import com.coocaa.detector.entity.Detector;
import com.coocaa.detector.entity.DetectorResult;
import org.springframework.stereotype.Component;

/**
 * @description: 失败处理
 * @author: dongyang_wu
 * @create: 2019-08-06 20:40
 */
@Component
public class IDetectorClientFallback implements IDetectorClient {
    @Override
    public R<DetectorResult> timeSeriesDetector(Detector detector) {
        return R.fail("获取数据失败");
    }
}