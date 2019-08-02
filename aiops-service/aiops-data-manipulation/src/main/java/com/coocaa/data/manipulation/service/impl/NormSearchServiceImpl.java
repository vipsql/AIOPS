package com.monitoring.data_manipulation.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.monitoring.data_manipulation.common.PromNorm;
import com.monitoring.data_manipulation.entity.MatrixData;
import com.monitoring.data_manipulation.entity.Metric;
import com.monitoring.data_manipulation.entity.PromMatrixData;
import com.monitoring.data_manipulation.entity.VectorData;
import com.monitoring.data_manipulation.service.NormSearchService;
import com.monitoring.data_manipulation.service.PromQLService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Auther: wyx
 * @Date: 2019-07-28 17:02
 * @Description:
 */
@Service
public class NormSearchServiceImpl implements NormSearchService {

    @Autowired
    private PromQLService promQLService;

    @Override
    public List<VectorData> queryInstantRequests(Date date, Integer timeout) {
        return promQLService.instantQuery(PromNorm.REQUEST_TOTAL, date, timeout);
    }

    @Override
    public List<MatrixData> queryRangeRequests(Date start, Date end, Integer step) {
        return promQLService.rangeQuery(PromNorm.REQUEST_TOTAL, start, end, step);
    }

    @Override
    public List<VectorData> queryInstantCPULoad(Date date, Integer timeout) {
        return promQLService.instantQuery(PromNorm.CPU_LOAD, date, timeout);
    }

    @Override
    public List<MatrixData> queryRangeCPULoad(Date start, Date end, Integer step) {
        return promQLService.rangeQuery(PromNorm.CPU_LOAD, start, end, step);
    }

    @Override
    public List<VectorData> queryInstantDisk(Date date, Integer timeout) {
        List<VectorData> rateDisk = new ArrayList<>();
        List<VectorData> totalDisk = promQLService.instantQuery(PromNorm.DISK_TOTAL_CAPACITY, date, timeout);
        List<VectorData> freeDisk = promQLService.instantQuery(PromNorm.DISK_FREE_CAPACITY, date, timeout);
        for(int i = 0; i < totalDisk.size(); i++){
            VectorData used = new VectorData();
            VectorData total = totalDisk.get(i);
            VectorData free = freeDisk.get(i);

            Metric metric = total.getMetric();
            metric.set__name__(PromNorm.DISK_USED_RATE);
            used.setMetric(metric);

            List<String> value = new ArrayList<>();
            value.add(total.getValue().get(0));

            String totalSizeStr = total.getValue().get(1);
            String freeSizeStr = free.getValue().get(1);
            BigDecimal totalSizeNum = new BigDecimal(totalSizeStr);
            BigDecimal freeSizeNum = new BigDecimal(freeSizeStr);
            BigDecimal usedSizeNum = totalSizeNum.subtract(freeSizeNum);
            double rate = usedSizeNum.divide(totalSizeNum, 4, RoundingMode.CEILING).doubleValue();
            value.add(String.valueOf(rate));
            
            used.setValue(value);
            rateDisk.add(used);
        }
        return rateDisk;
    }

    @Override
    public List<MatrixData> queryRangeDisk(Date start, Date end, Integer step) {
        List<MatrixData> rateDisk = new ArrayList<>();
        List<MatrixData> totalDisk = promQLService.rangeQuery(PromNorm.DISK_TOTAL_CAPACITY, start, end, 15);
        List<MatrixData> freeDisk = promQLService.rangeQuery(PromNorm.DISK_FREE_CAPACITY, start, end, 15);
        for(int i = 0; i < totalDisk.size(); i++){
            MatrixData matrixData = new MatrixData();
            MatrixData total = totalDisk.get(i);
            MatrixData free = freeDisk.get(i);

            Metric metric = total.getMetric();
            metric.set__name__(PromNorm.DISK_USED_RATE);
            matrixData.setMetric(metric);

            List<String> totals = total.getValues();
            List<String> frees = free.getValues();
            List<String> used = new ArrayList<>();
            for(int j = 0; j < totals.size(); j++){
                List<String> usedValues = new ArrayList<>();
                JSONArray totalArray = JSONObject.parseArray(totals.get(j));
                List<String> totalValues = JSONObject.parseArray(totalArray.toJSONString(), String.class);
                JSONArray freeArray = JSONObject.parseArray(frees.get(j));
                List<String> freeValues = JSONObject.parseArray(freeArray.toJSONString(), String.class);
                usedValues.add(totalValues.get(0));
                BigDecimal totalDecimal = new BigDecimal(totalValues.get(1));
                BigDecimal freeDecimal = new BigDecimal(freeValues.get(1));
                BigDecimal userDecimal = totalDecimal.subtract(freeDecimal);
                double rate = userDecimal.divide(totalDecimal, 4, RoundingMode.CEILING).doubleValue();
                usedValues.add(String.valueOf(rate));

                used.add(usedValues.toString());
            }

            matrixData.setValues(used);
            rateDisk.add(matrixData);
        }

        return rateDisk;
    }

    @Override
    public List<VectorData> queryInstantUpLoadBandWidth(Date date, Integer timeout) {
        return promQLService.instantQuery(PromNorm.UPLOAD_BAND_WIDTH, date, timeout);
    }

    @Override
    public List<MatrixData> queryRangeUpLoadBandWidth(Date start, Date end, Integer step) {
        return promQLService.rangeQuery(PromNorm.UPLOAD_BAND_WIDTH, start, end, step);
    }

    @Override
    public List<VectorData> queryInstantDownLoadBandWidth(Date date, Integer timeout) {
        return promQLService.instantQuery(PromNorm.DOWNLOAD_BAND_WIDTH, date, timeout);
    }

    @Override
    public List<MatrixData> queryRangeDownLoadBandWidth(Date start, Date end, Integer step) {
        return promQLService.rangeQuery(PromNorm.DOWNLOAD_BAND_WIDTH, start, end, step);
    }

    @Override
    public PromMatrixData queryRangeData(Date start, Date end, Integer step) {
        List<MatrixData> requests = queryRangeRequests(start, end, step);
        List<MatrixData> disk = queryRangeDisk(start, end, step);
        List<MatrixData> cpuLoad = queryRangeCPULoad(start, end, step);
        List<MatrixData> upLoadBandWidth = queryRangeUpLoadBandWidth(start, end, step);
        List<MatrixData> downLoadBandWidth = queryRangeDownLoadBandWidth(start, end, step);
        return new PromMatrixData(requests, disk, cpuLoad, upLoadBandWidth, downLoadBandWidth);
    }
}
