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
     *
     * @param detector 传入检测数据
     * @return
     */
    DetectorResult detection(Detector detector);

    /**
     * 提交训练集，开始训练模型
     *
     * @param train
     * @return 返回任务提交成功与否
     */
    boolean train(Train train);

    /**
     * 获取全部训练资源集合名字
     *
     * @return
     */
    String[] getAllQueryTrainSource();

    /**
     * 获取全部模型名
     *
     * @return
     */
    String[] getAllModelSource();

    /**
     * 按资源名获取模型资源名
     *
     * @param sourceName
     * @return
     */
    String[] getModelSource(String sourceName);

    /**
     * 获取模型训练状态，用于异步检测模型训练情况
     *
     * @param str
     * @return
     */
    String getModelStatus(String str);
}