package com.coocaa.detector.controller;

import com.coocaa.core.log.response.ResponseHelper;
import com.coocaa.core.log.response.ResultBean;
import com.coocaa.core.tool.response.ReturnData;
import com.coocaa.detector.entity.*;
import com.coocaa.detector.servcie.MetisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author 陈煜坤
 * @date 2019/7/30  14:59
 * @package_name com.monitoring.warn.notice.controller
 */
@RestController
@Api(value = "异常检测模块", tags = "异常检测接口")
@AllArgsConstructor
public class MetisController {

    MetisService metisService;

    @PostMapping("/metis/detector")
    @ApiOperation(value = "异常检测")
    public ResponseEntity<ResultBean> timeSeriesDetector(@RequestBody Detector detector) {
        DetectorResult detectorResult = metisService.detection(detector);
        return ResponseHelper.OK(detectorResult);
    }
}