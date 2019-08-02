package com.monitoring.data_manipulation.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.common.parent_model.common.CodeEnum;
import com.common.parent_model.exception.BaseException;
import com.monitoring.data_manipulation.entity.MatrixData;
import com.monitoring.data_manipulation.entity.VectorData;
import com.monitoring.data_manipulation.service.PromQLService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Auther: wyx
 * @Date: 2019-07-28 0:01
 * @Description:
 */
@Service
@Slf4j
public class PromQLServiceImpl implements PromQLService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${web.server.prometheus.port}")
    private String serverUrl;

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss.SSS");

    @Override
    public List<VectorData> instantQuery(String query, Date date, Integer timeout) {
        if (date == null){
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

        List<String> urlParams = parseUrl(url);

        JSONObject dataJson = getQueryResult(urlParams.get(0), urlParams.get(1));

        return parseMatrix(dataJson);
    }

    @Override
    public String metadataQuery(List<String> match, Date start, Date end) {
        return null;
    }

    @Override
    public String labelQuery(String label) {
        return null;
    }

    private JSONObject verifyResponse(ResponseEntity<String> forEntity){
        HttpStatus statusCode = forEntity.getStatusCode();
        if (statusCode.value() != 200){
            throw new BaseException(CodeEnum.PROMETHEUS_QUERY_ERROR.getCode(), CodeEnum.PROMETHEUS_QUERY_ERROR.getMsg());
        }

        String body = forEntity.getBody();
        JSONObject jsonObject = JSON.parseObject(body);
        if (!jsonObject.get("status").toString().equals("success")){
            throw new BaseException(CodeEnum.PROMETHEUS_QUERY_ERROR.getCode(), CodeEnum.PROMETHEUS_QUERY_ERROR.getMsg());
        }

        return jsonObject;
    }

    private long parseTime(Date date){
        return date.getTime() / 1000;
    }

    private List<String> parseUrl(String url){
        List<String> urlParams = new ArrayList<>();
        if(url.contains("{") && url.contains("}")){
            String json = "{" + url.substring(url.indexOf("{") + 1, url.indexOf("}")) + "}";
            String newUrl = url.substring(0, url.indexOf("{") + 1) + "json" + url.substring(url.indexOf("}"));
            urlParams.add(newUrl);
            urlParams.add(json);
        }else{
            urlParams.add(url);
            urlParams.add(null);
        }
        return urlParams;
    }

    private JSONObject getQueryResult(String url, String json){
        log.info("url: {}", url);
        log.info("json: {}", json);
        ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class, json);

        JSONObject jsonObject = verifyResponse(forEntity);

//        String resultType = String.valueOf(dataJson.get("resultType"));
        return JSON.parseObject(String.valueOf(jsonObject.get("data")));
    }

    private List<MatrixData> parseMatrix(JSONObject dataJson){
        JSONArray jsonArray = new JSONArray(dataJson.getJSONArray("result"));
        return JSONObject.parseArray(jsonArray.toJSONString(), MatrixData.class);
    }

    private List<VectorData> parseVector(JSONObject dataJson){
        JSONArray jsonArray = new JSONArray(dataJson.getJSONArray("result"));
        return JSONObject.parseArray(jsonArray.toJSONString(), VectorData.class);
    }

    private void parseScalar(JSONObject dataJson){

    }

    private void parseString(JSONObject dataJson){

    }

}
