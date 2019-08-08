package com.coocaa.detector.feign;

import com.coocaa.core.tool.api.R;
import com.coocaa.detector.entity.*;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

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

    @Override
    public R<Boolean> train(Train train) {
        return R.fail("获取数据失败");
    }

    @Override
    public R<String> importSample(Map<String, Object> metrics) {
        return R.fail("获取数据失败");
    }

    @Override
    public R<String[]> getModelSource() {
        return R.fail("获取数据失败");
    }

    @Override
    public R<String[]> getModelSource(String sourceName) {
        return R.fail("获取数据失败");
    }

    @Override
    public R<String[]> getQueryTrainSource() {
        return R.fail("获取数据失败");
    }

    @Override
    public R<List<String>> getQueryTrainSource(String sourceName) {
        return R.fail("获取数据失败");
    }

    @Override
    public R<String> getModelStatus(String modelName) {
        return R.fail("获取数据失败");
    }
}