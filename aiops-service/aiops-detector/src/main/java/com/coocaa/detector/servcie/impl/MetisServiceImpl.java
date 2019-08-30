package com.coocaa.detector.servcie.impl;

import com.alibaba.fastjson.*;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coocaa.detector.entity.*;
import com.coocaa.detector.mapper.*;
import com.coocaa.detector.servcie.MetisService;
import com.coocaa.detector.util.metis.*;
import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * @author 陈煜坤
 * @date 2019/7/30  15:01
 * @package_name com.monitoring.warn.notice.service.impl
 */
@Service
public class MetisServiceImpl implements MetisService {
    @Value("${metis.start.bootrap}")
    public String HTTPPATH;
    @Autowired
    private TimeSeriesDetector timeSeriesDetector;
    @Autowired
    private MetisSourceMapper metisSourceMapper;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    TrainMapper trainMapper;
    @Autowired
    SampleDatasetMapper sampleDatasetMapper;

    @Override
    public DetectorResult detection(Detector detector) {
        DetectorResult detect = timeSeriesDetector.detect(detector);
        return detect;
    }

    @Override
    public String train(Train train) {
        String toJSONString = JSON.toJSONString(train);
        String postForObject = restTemplate.postForObject(HTTPPATH + "/CountSample", train, String.class);
        Count count;
        JSONArray jsonArray = JSON.parseObject(postForObject).getJSONObject("data").getJSONArray("count");
        List<Count> counts = JSON.parseArray(jsonArray.toJSONString(), Count.class);
        count = counts.get(0);
//            count = MyJSON.jsonToObject(postForObject, Count.class);
        if (count.getTotal_count() == 0) {
            return "数据错误";
        }
        if (count.getNegative_count() == 0) {
            return "数据错误";
        }
        if (count.getPositive_count() == 0) {
            return "数据错误";
        }
        rabbitTemplate.convertAndSend("metis", "metis.train", toJSONString);
        String s = showTimeForecast(count.getTotal_count());
        train.setExTime(Integer.parseInt(s));
        trainMapper.insert(train);
        return s;
    }

    @Override
    public String putTrain(Train train) {
        if ("complete".equals(train.getStutas())) {
            train.setStutas("mark");
        }
        trainMapper.updateById(train);
        return "OK!";
    }

    @Override
    public String deleteTrain(Train train) {
        trainMapper.deleteById(train.getId());
        return "删除成功!";
    }

    @Override
    public List<SampleDataset> getSampleDataset(QuerySample querySample) {
        IPage<SampleDataset> iPage = new Page<>(querySample.getCurrent(), querySample.getSize());
        QueryWrapper<SampleDataset> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(querySample.getData())) {
            queryWrapper.eq("source", querySample.getData());
        }
        if (StringUtils.isNotBlank(querySample.getPositiveOrNegative())) {
            queryWrapper.eq("positive_or_negative", querySample.getPositiveOrNegative());
        }
        if (querySample.getBeginTime() > 0) {
            queryWrapper.ge("begin_time", querySample.getBeginTime());
        }
        IPage<SampleDataset> iPage1 = sampleDatasetMapper.selectPage(iPage, queryWrapper);
        List<SampleDataset> records = iPage1.getRecords();
        return records;
    }

    @Override
    public void putSampleDataset(List<SampleDataset> sds) {
        for (SampleDataset sd : sds) {
            sampleDatasetMapper.updateById(sd);
        }
    }

    @Override
    public void deleteSampleDataset(SampleDataset sd) {
        sampleDatasetMapper.deleteById(sd.getId());
    }

    @Override
    public String beginTrain(Train train) {
        String toJSONString = JSON.toJSONString(train);
        restTemplate.postForObject(HTTPPATH + "/Train", toJSONString, String.class);
        train.setStutas("training");
        return "开始训练!";
    }

    @Override
    public String[] getAllQueryTrainSource() {
        String[] allQueryTrainSource = metisSourceMapper.getAllQueryTrainSource();
        return allQueryTrainSource;
    }

    @Override
    public String[] getAllModelSource() {
        String[] allModelSource = metisSourceMapper.getAllModelSource();
        for (int i = 0; i < allModelSource.length; i++) {
            allModelSource[i] = SourceUtils.modifyModelSuffixName(allModelSource[i]);
        }
        return allModelSource;
    }

    @Override
    public List<Model> getAllModel(int current, int size) {
        Page<Model> page = new Page<>(current, size);
        List<Model> allModel = metisSourceMapper.getAllModel(page);
        for (int i = 0; i < allModel.size(); i++) {
            allModel.get(i).setModelName(SourceUtils.modifyModelSuffixName(allModel.get(i).getModelName()));
        }
        return allModel;
    }

    @Override
    public String[] getModelSource(String sourceName) {
        String[] modelSource = metisSourceMapper.getModelSource(sourceName);
        for (int i = 0; i < modelSource.length; i++) {
            modelSource[i] = SourceUtils.modifyModelSuffixName(modelSource[i]);
        }
        return modelSource;
    }

    @Override
    public String getModelStatus(String str) {
        String modelStatus = metisSourceMapper.getModelStatus(str);
        return modelStatus;
    }

    @Override
    public String showTimeForecast(int sum) {
        List<Model> allModel = metisSourceMapper.getAllModelTime();
        String msg = "-1";
        if (allModel != null) {
            Model m = allModel.get(0);
            for (int i = 1; i < allModel.size(); i++) {
                if (Math.abs(sum - m.getSampleNum()) > Math.abs(sum - allModel.get(i).getSampleNum())) {
                    m = allModel.get(i);
                }
            }
            if ((sum - m.getSampleNum()) <= 0) {
                double i = Double.parseDouble(sum + "") / m.getSampleNum();
                String str = String.valueOf((m.getEndTime().getTime() - m.getStartTime().getTime()) * i);
                return str.substring(0, str.indexOf("."));
            } else {
                double i = Double.parseDouble(sum + "") / m.getSampleNum();
                String str = String.valueOf((m.getEndTime().getTime() - m.getStartTime().getTime()) * i);
                return str.substring(0, str.indexOf("."));
            }
        }
        return msg;
    }

    @Override
    public List<Train> getTrain(int current, int size, Train train) {
        IPage<Train> iPage = new Page<>(current, size);
        QueryWrapper<Train> trainQueryWrapper = new QueryWrapper<>();

        if (train.getSource().size() > 0) {
            trainQueryWrapper.like("source", train.getSource().get(0));
        }
        if (StringUtils.isNotBlank(train.getModelName())) {
            trainQueryWrapper.like("model_name", train.getModelName());
        }
        if (train.getBeginTime() > 0) {
            trainQueryWrapper.ge(true, "begin_time", train.getBeginTime());
        }
        if (train.getEndTime() > 0) {
            trainQueryWrapper.le(true, "end_time", train.getEndTime());
        }
        if (train.getTimeInterval() > 0) {
            trainQueryWrapper.eq("time_interval", train.getTimeInterval());
        }
        IPage<Train> iPage1 = trainMapper.selectPage(iPage, trainQueryWrapper);
        List<Train> records = iPage1.getRecords();
        return records;
    }

}