package com.coocaa.prometheus.service.impl;

import com.alibaba.fastjson.*;
import com.coocaa.common.constant.Constant;
import com.coocaa.core.log.exception.ApiException;
import com.coocaa.core.log.exception.ApiResultEnum;
import com.coocaa.core.log.response.ResponseHelper;
import com.coocaa.core.log.response.ResultBean;
import com.coocaa.core.tool.api.R;
import com.coocaa.core.tool.base.BaseException;
import com.coocaa.core.tool.response.CodeEnum;
import com.coocaa.core.tool.utils.*;
import com.coocaa.detector.entity.*;
import com.coocaa.detector.feign.IDetectorFeign;
import com.coocaa.prometheus.common.PromNorm;
import com.coocaa.prometheus.entity.*;
import com.coocaa.prometheus.input.QueryMetricProperty;
import com.coocaa.prometheus.output.httpRequestToTal.MatrixResult;
import com.coocaa.prometheus.service.PromQLService;
import com.coocaa.prometheus.util.PromQLUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Auther: wyx
 * @Date: 2019-07-28 0:01
 * @Description:
 */
@Service("PromQLService")
@Slf4j
public class PromQLServiceImpl implements PromQLService {
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private IDetectorFeign detectorFeign;
    @Value("${web.server.prometheus.apiUrl}")
    private String serverUrl;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss.SSS");
    private SimpleDateFormat metisDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public ResponseEntity<ResultBean> queryMetrics(Task task, Integer type) {
        if (Constant.NumberType.ZERO_PROPERTY.equals(type)) {
            QueryInstant queryInstant = task.getQueryInstant();
            return ResponseHelper.OK(instantQuery(queryInstant.getQuery(), queryInstant.getDate(), queryInstant.getTimeout()));
        } else if (Constant.NumberType.ONE_PROPERTY.equals(type)) {
            QueryRange queryRange = task.getQueryRange();
            List<MatrixData> matrixData = rangeQuery(queryRange.getQuery(), queryRange.getStart(), queryRange.getEnd(), queryRange.getStep());
            return ResponseHelper.OK(matrixData);
        }
        throw new ApiException(ApiResultEnum.FUNCTION_NOT_EXEC_ERROR);
    }

    @Override
    public ResponseEntity<ResultBean> exceptionDetect(QueryMetricProperty queryMetricProperty, Integer type) {
        String realQuery = null;
        if (Constant.NumberType.ZERO_PROPERTY.equals(type)) {
            queryMetricProperty.setMetricsName(PromNorm.REQUEST_TOTAL);
            realQuery = getRealHttpTotalQuery(queryMetricProperty);
        } else if (Constant.NumberType.ONE_PROPERTY.equals(type)) {
            realQuery = PromNorm.UPLOAD_BAND_WIDTH;
        } else if (Constant.NumberType.TWO_PROPERTY.equals(type)) {
            realQuery = PromNorm.DOWNLOAD_BAND_WIDTH;
        } else if (Constant.NumberType.THREE_PROPERTY.equals(type)) {
            queryMetricProperty.setMetricsName(PromNorm.NODE_SOCKSTAT_TCP_ALLOC);
            realQuery = getRealHttpTotalQuery(queryMetricProperty);
        } else if (Constant.NumberType.FOUR_PROPERTY.equals(type)) {
            queryMetricProperty.setMetricsName(PromNorm.NODE_CPU_SECONDS_TOTAL);
            realQuery = getRealHttpTotalQuery(queryMetricProperty);
        } else if (Constant.NumberType.FIVE_PROPERTY.equals(type)) {
            realQuery = getRealHttpTotalQuery(queryMetricProperty);
        } else if (Constant.NumberType.SIX_PROPERTY.equals(type)) {
            queryMetricProperty.setMetricsName(PromNorm.NODE_DISK_IO_TIME_SECONDS_TOTAL);
            realQuery = getRealHttpTotalQuery(queryMetricProperty);
        } else if (Constant.NumberType.SEVEN_PROPERTY.equals(type)) {
            queryMetricProperty.setMetricsName(PromNorm.NODE_NETWORK_RECEIVE_BYTES_TOTAL);
            realQuery = getRealHttpTotalQuery(queryMetricProperty);
        } else if (Constant.NumberType.EIGHT_PROPERTY.equals(type)) {
            queryMetricProperty.setMetricsName(PromNorm.NODE_MEMORY_CACHED_BYTES);
            realQuery = getRealHttpTotalQuery(queryMetricProperty);
        }
        Date now = new Date();
        System.out.println("-----------当前时刻");
        Date date = DateUtil.setHours(now, -3);
        List<MatrixData> metisDataA = getMetisDataFirst(realQuery, date, now);
        System.out.println("------------一天前");
        Date yesterday = DateUtil.setDays(now, -1);
        List<String> metisDataB = getMetisData(realQuery, DateUtil.setHours(yesterday, -3), DateUtil.setHours(yesterday, 3));
        System.out.println("------------一周前");
        Date weekEarlier = DateUtil.setWeeks(now, -1);
        List<String> metisDataC = getMetisData(realQuery, DateUtil.setHours(weekEarlier, -3), DateUtil.setHours(weekEarlier, 3));
        List<MatrixResult> realResult = new ArrayList<>();
        for (int i = 0; i < metisDataA.size(); i++) {
            MatrixData matrixData = metisDataA.get(i);
            MatrixResult matrixResult = new MatrixResult();
            matrixResult.setMetric(matrixData.getMetric());
            Detector detector = Detector.builder()
                    .attrName("http接口请求数")
                    .window(180)
                    .viewId("2012")
                    .viewName("登陆功能")
                    .attrId("19201")
                    .dataA(matrixData.getMetisData())
                    .dataB(metisDataB.get(i))
                    .dataC(metisDataC.get(i))
                    .time(metisDateFormat.format(now)).build();
            R<DetectorResult> rpcResult = detectorFeign.timeSeriesDetector(detector);
            if (rpcResult.isSuccess()) {
                matrixResult.getMetric().set__name__(realQuery);
                matrixResult.setDetectResult(rpcResult.getData().getData());
                realResult.add(matrixResult);
            }
        }
        return ResponseHelper.OK(realResult);
    }

    @Override
    public ResponseEntity<ResultBean> getTargets() {
        String url = serverUrl + "targets";
        ResponseEntity<String> targetsApiResult = restTemplate.getForEntity(url, String.class);
        JSONObject jsonObject = verifyResponse(targetsApiResult);
        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("activeTargets");
        List<Targets> targets = JSON.parseArray(jsonArray.toJSONString(), Targets.class);
        return ResponseHelper.OK(targets);
    }

    @Override
    public List<VectorData> instantQuery(String query, Date date, Integer timeout) {
        if (date == null) {
            date = new Date();
        }
        String url = serverUrl + "query?query=" + query + "&time=" + (date.getTime() / 1000) + (timeout == null ? "" : ("&timeout=" + timeout));

        List<String> urlParams = parseUrl(url);

        JSONObject dataJson = getQueryResult(urlParams.get(0), urlParams.get(1));
        return parseVector(dataJson);
    }

    @Override
    public List<MatrixData> rangeQuery(String query, Date start, Date end, Integer step) {
        String url = serverUrl + "query_range?query=" + query + "&start=" + parseTime(start) + "&end=" + parseTime(end) + "&step=" + step;
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        URI uri = builder.build().encode().toUri();
        ResponseEntity<String> forEntity = restTemplate.getForEntity(uri, String.class);
        JSONObject jsonObject = verifyResponse(forEntity);
        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("result");
        List<MatrixData> matrixData = JSON.parseArray(jsonArray.toJSONString(), MatrixData.class);
        return matrixData;
    }

    private String getRealHttpTotalQuery(QueryMetricProperty queryMetricProperty) {
        String instance = queryMetricProperty.getInstance();
        String request = queryMetricProperty.getRequest();
        String status = queryMetricProperty.getStatus();
        List<String> conditionQuery = new ArrayList<>();
        if (Func.isNotEmpty(instance)) {
            conditionQuery.add(PromQLUtil.getQueryConditionStr(QueryMetricProperty.HttpRequestsTotalNameKey.INSTANCE, instance));
        }
        if (Func.isNotEmpty(request)) {
            conditionQuery.add(PromQLUtil.getQueryConditionStr(QueryMetricProperty.HttpRequestsTotalNameKey.REQUEST, request));
        }
        if (Func.isNotEmpty(status)) {
            conditionQuery.add(PromQLUtil.getQueryConditionStr(QueryMetricProperty.HttpRequestsTotalNameKey.STATUS, status));
        }
        if (!CollectionUtils.isEmpty(conditionQuery)) {
            StringBuffer condition = new StringBuffer();
            condition.append("{").append(StringUtils.collectionToDelimitedString(conditionQuery, ",")).append("}");
            return String.format(queryMetricProperty.getMetricsName(), condition.toString());
        }
        return queryMetricProperty.getMetricsName().replace("%s", "");
    }

    private List<MatrixData> getMetisDataFirst(String query, Date begin, Date end) {
        List<MatrixData> matrixData = rangeQuery(query, begin, end, 60);
        matrixData.forEach(result -> {
            StringBuffer sb = new StringBuffer();
            result.getValues().forEach(item -> sb.append(getRealMetisValue(item)));
            result.setMetisData(sb.substring(0, sb.length() - 1));
        });
        return matrixData;
    }

    private List<String> getMetisData(String query, Date begin, Date end) {
        List<MatrixData> matrixData = rangeQuery(query, begin, end, 60);
        List<String> metisString = new ArrayList<>();
        matrixData.forEach(result -> {
            StringBuffer sb = new StringBuffer();
            result.getValues().forEach(item -> sb.append(getRealMetisValue(item)));
            metisString.add(sb.substring(0, sb.length() - 1));
        });
        return metisString;
    }

    private String getRealMetisValue(String item) {
        String substring = item.substring(item.indexOf(",") + 2, item.indexOf("]") - 1);
        if (substring.contains(".")) {
            substring = String.format("%.4f", Double.parseDouble(substring));
            Double aDouble = Double.valueOf(substring);
            aDouble = aDouble * 10000;
            return aDouble.intValue() + ",";
        } else if (substring.equals("0")) {
            substring = "10000";
            return substring + ",";
        }
        return substring + ",";
    }

    private JSONObject verifyResponse(ResponseEntity<String> forEntity) {
        HttpStatus statusCode = forEntity.getStatusCode();
        if (statusCode.value() != 200) {
            throw new BaseException(CodeEnum.PROMETHEUS_QUERY_ERROR.getCode(), CodeEnum.PROMETHEUS_QUERY_ERROR.getMsg());
        }
        String body = forEntity.getBody();
        JSONObject jsonObject = JSON.parseObject(body);
        if (!jsonObject.get("status").toString().equals("success")) {
            throw new BaseException(CodeEnum.PROMETHEUS_QUERY_ERROR.getCode(), CodeEnum.PROMETHEUS_QUERY_ERROR.getMsg());
        }
        return jsonObject;
    }

    private long parseTime(Date date) {
        return date.getTime() / 1000;
    }

    private List<String> parseUrl(String url) {
        List<String> urlParams = new ArrayList<>();
        if (url.contains("{") && url.contains("}")) {
            String json = "{" + url.substring(url.indexOf("{") + 1, url.indexOf("}")) + "}";
            String newUrl = url.substring(0, url.indexOf("{") + 1) + "json" + url.substring(url.indexOf("}"));
            urlParams.add(newUrl);
            urlParams.add(json);
        } else {
            urlParams.add(url);
            urlParams.add(null);
        }
        return urlParams;
    }

    private JSONObject getQueryResult(String url, String json) {
        log.info("url: {}", url);
        log.info("json: {}", json);
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class, json);

        JSONObject jsonObject = verifyResponse(forEntity);
//        String resultType = String.valueOf(dataJson.get("resultType"));
        return JSON.parseObject(String.valueOf(jsonObject.get("data")));
    }

    private List<MatrixData> parseMatrix(JSONObject dataJson) {
        return JSONObject.parseArray(dataJson.getJSONArray("result").toJSONString(), MatrixData.class);
    }

    private List<VectorData> parseVector(JSONObject dataJson) {
        JSONArray jsonArray = new JSONArray(dataJson.getJSONArray("result"));
        return JSONObject.parseArray(jsonArray.toJSONString(), VectorData.class);
    }

    @Override
    public String metadataQuery(List<String> match, Date start, Date end) {
        return null;
    }

    @Override
    public String labelQuery(String label) {
        return null;
    }

    private void parseScalar(JSONObject dataJson) {

    }

    private void parseString(JSONObject dataJson) {

    }

}
