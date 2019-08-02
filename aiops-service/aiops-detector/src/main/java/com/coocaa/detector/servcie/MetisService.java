package com.coocaa.detector.servcie;

import com.coocaa.detector.entity.*;

/**
 * @author 陈煜坤
 * @date 2019/7/30  15:01
 * @package_name com.monitoring.warn.notice.service
 */
public interface MetisService {

    /**
     * 对检测数据进行检验并返回检测结果
     * @param detector 传入检测数据
     * @return
     */
    DetectorResult detection(Detector detector);
}