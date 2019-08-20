package com.coocaa.detector.controller;

import com.alibaba.fastjson.JSON;
import com.coocaa.core.tool.response.ReturnData;
import com.coocaa.detector.entity.*;
import com.coocaa.detector.servcie.MetisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author 陈煜坤
 * @date 2019/7/30  14:59
 * @package_name com.monitoring.warn.notice.controller
 */
@RestController
public class MetisController {

    @Autowired
    MetisService metisService;

    @Autowired
    RestTemplate restTemplate;

    @Value("${metis.start.bootrap}")
    public String HTTPPATH;

    @PostMapping("/metis/detector")
    public ReturnData timeSeriesDetector(Detector detector){
        DetectorResult detection = metisService.detection(detector);
        return ReturnData.success(detection);
    }

    @GetMapping("/Train/{current}/{size}")
    public ReturnData getTrain(@RequestBody Train train, @PathVariable int current, @PathVariable int size){
        List<Train> b = metisService.getTrain(current,size,train);
        return ReturnData.success(b);
    }
    @PostMapping("/Train")
    public ReturnData postTrain(@RequestBody Train train){
        String b = metisService.train(train);
        if (!b.equals("数据错误")){
            return ReturnData.success(b);
        }else {
            return ReturnData.fail(500,"提交模型训练失败,请检测所所选范围内是否有数据，或者是否都拥有正负样本数据");
        }
    }
    @PutMapping("/Train")
    public ReturnData putTrain(@RequestBody Train train){
        String b = metisService.putTrain(train);
        return ReturnData.success(b);

    }
    @PutMapping("/beginTrain")
    public ReturnData beginTrain(@RequestBody Train train){
        String b = metisService.beginTrain(train);
        return ReturnData.success(b);
    }
    @DeleteMapping("/Train")
    public ReturnData deleteTrain(@RequestBody Train train){
        String b = metisService.deleteTrain(train);
        return ReturnData.success(b);
    }

    @PostMapping("/ImportSample")
    public ReturnData ImportSample(@RequestBody Map<String,Object> metrics){
        restTemplate.postForObject(HTTPPATH+"/ImportSample", JSON.toJSONString(metrics),String.class);
        return ReturnData.success();
    }

    @GetMapping("/ModelSource")
    public ReturnData getModelSource(){
        String[] allModelSource = metisService.getAllModelSource();
        return ReturnData.success(allModelSource);
    }

    @GetMapping("/ModelSource/{sourceName}")
    public ReturnData getModelSource(@PathVariable("sourceName") String sourceName){
        String[] allModelSource = metisService.getModelSource(sourceName);
        return ReturnData.success(allModelSource);
    }

    @GetMapping("/QueryTrainSource")
    public ReturnData getQueryTrainSource(){
        String[] allQueryTrainSource = metisService.getAllQueryTrainSource();
        return ReturnData.success(allQueryTrainSource);
    }
    @GetMapping("/QueryTrainSource/{sourceName}")
    public ReturnData getQueryTrainSource(@PathVariable("sourceName") String sourceName){
        String[] allQueryTrainSource = metisService.getAllQueryTrainSource();
        List<String> list = new ArrayList<>();
        for (String s : allQueryTrainSource) {
            if(s.contains(sourceName)){
                list.add(s);
            }
        }
        return ReturnData.success(list);
    }

    @GetMapping("/ModelStatus/{modelName}")
    public ReturnData getModelStatus(@PathVariable("modelName") String modelName){
        String modelStatus = metisService.getModelStatus(modelName);
        return ReturnData.success(modelStatus);
    }
    @GetMapping("/getAllModel/{current}/{size}")
    public ReturnData getAllModel(@PathVariable("current") int current,
                                  @PathVariable("size") int size){
        if (current<0 || size < 0){
            return ReturnData.success();
        }
        List<Model> allModel = metisService.getAllModel(current, size);
        return ReturnData.success(allModel);
    }

    @GetMapping("/SampleDataset")
    public ReturnData getQuerySample(@RequestBody QuerySample qs){
        List<SampleDataset> sampleDataset = metisService.getSampleDataset(qs);
        return ReturnData.success(sampleDataset);
    }
    @PutMapping("/SampleDataset")
    public ReturnData postQuerySample(@RequestBody List<SampleDataset> sds){
        metisService.putSampleDataset(sds);
        return ReturnData.success("保存成功!");
    }
    @DeleteMapping("/SampleDataset")
    public ReturnData postQuerySample(@RequestBody SampleDataset sd){
        metisService.deleteSampleDataset(sd);
        return ReturnData.success("删除成功!");
    }
}
