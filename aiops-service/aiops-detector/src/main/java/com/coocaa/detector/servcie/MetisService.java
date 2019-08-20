package com.coocaa.detector.servcie;

import com.coocaa.detector.entity.*;

import java.util.List;

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
     * @param train
     * @return 返回任务提交成功与否
     */
    String train(Train train);

    /**
     * 获取全部训练资源集合名字
     * @return
     */
    String[] getAllQueryTrainSource();

    /**
     * 获取全部模型名
     * @return
     */
    String[] getAllModelSource();
    /**
     * 获取全部模型
     * @return
     */
    List<Model> getAllModel(int current,int size);

    /**
     * 按资源名获取模型资源名
     * @param sourceName
     * @return
     */
    String[] getModelSource(String sourceName);

    /**
     * 获取模型训练状态，用于异步检测模型训练情况
     * @param str
     * @return
     */
    String getModelStatus(String str);

    /**
     * 显示时间预测
     * @return
     */
    String showTimeForecast(int sum);

    /**
     * 获取显示模型集合
     * @param train
     * @return
     */
    List<Train> getTrain(int current, int size, Train train);

    /**
     * 更新模型训练
     * @param train
     * @return
     */
    String putTrain(Train train);

    /**
     * 开始训练
     * @param train
     * @return
     */
    String beginTrain(Train train);

    /**
     * 删除模型信息
     * @param train
     * @return
     */
    String deleteTrain(Train train);

    List<SampleDataset> getSampleDataset(QuerySample querySample);

    void putSampleDataset(List<SampleDataset> sds);

    void deleteSampleDataset(SampleDataset sd);
}