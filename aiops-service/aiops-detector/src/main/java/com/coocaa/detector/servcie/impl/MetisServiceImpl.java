package com.coocaa.detector.servcie.impl;

import com.alibaba.fastjson.*;
import com.coocaa.detector.entity.*;
import com.coocaa.detector.mapper.MetisSourceMapper;
import com.coocaa.detector.servcie.MetisService;
import com.coocaa.detector.util.metis.*;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
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

    @Override
    public DetectorResult detection(Detector detector) {
        DetectorResult detect = timeSeriesDetector.detect(detector);
        return detect;
    }

    @Override
    public boolean train(Train train) {
        String toJSONString = JSON.toJSONString(train);
        String postForObject = restTemplate.postForObject(HTTPPATH + "/CountSample", train, String.class);
        Count count = null;
        try {
            JSONArray jsonArray = JSON.parseObject(postForObject).getJSONObject("data").getJSONArray("count");
            List<Count> counts = JSON.parseArray(jsonArray.toJSONString(), Count.class);
            count = counts.get(0);
//            count = MyJSON.jsonToObject(postForObject, Count.class);
        } catch (Exception e) {
        }
        if (count.getTotal_count() == 0) {
            return false;
        }
        if (count.getNegative_count() == 0) {
            return false;
        }
        if (count.getPositive_count() == 0) {
            return false;
        }
        rabbitTemplate.convertAndSend("metis", "metis.train", toJSONString);
        return true;
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

    @RabbitListener(queues = "metis.train")
    public void trainListen(String json) {
        restTemplate.postForObject(HTTPPATH + "/Train", json, String.class);
    }
}