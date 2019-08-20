package com.coocaa.prometheus.service.impl;

import com.alibaba.fastjson.*;
import com.coocaa.common.constant.Constant;
import com.coocaa.common.request.*;
import com.coocaa.core.log.exception.ApiException;
import com.coocaa.core.log.exception.ApiResultEnum;
import com.coocaa.core.log.response.ResponseHelper;
import com.coocaa.core.log.response.ResultBean;
import com.coocaa.core.secure.utils.SecureUtil;
import com.coocaa.core.tool.api.R;
import com.coocaa.core.tool.base.BaseException;
import com.coocaa.core.tool.response.CodeEnum;
import com.coocaa.core.tool.utils.*;
import com.coocaa.detector.entity.*;
import com.coocaa.detector.feign.IDetectorClient;
import com.coocaa.prometheus.common.PromBaseLables;
import com.coocaa.prometheus.common.PromNorm;
import com.coocaa.prometheus.dto.MetisDto;
import com.coocaa.prometheus.entity.*;
import com.coocaa.prometheus.input.QueryMetricProperty;
import com.coocaa.prometheus.mapper.KpiMapper;
import com.coocaa.prometheus.mapper.TaskMapper;
import com.coocaa.prometheus.output.MetricsCsvVo;
import com.coocaa.prometheus.output.TaskOutputVo;
import com.coocaa.prometheus.service.PromQLService;
import com.coocaa.prometheus.util.PromQLUtil;
import com.coocaa.user.entity.User;
import com.coocaa.user.feign.IUserClient;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

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
    private IDetectorClient detectorFeign;
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private IUserClient userClient;
    @Autowired
    private KpiMapper kpiMapper;
    @Value("${web.server.prometheus.apiUrl}")
    private String serverUrl;

    private SimpleDateFormat metisDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public ResponseEntity<ResultBean> listByPage(PageWithTeamRequestBean pageRequestBean) {
        RequestUtil.setDefaultPageBean(pageRequestBean);
        String conditionString = SqlUtil.getConditionString(pageRequestBean.getConditions(), pageRequestBean.getConditionConnection());
//        String teamIds = userClient.userById(SecureUtil.getUserId()).getData().getTeamIds();
//        // 增加条件 用户的TeamIds在task的Teamids下
//        conditionString = SqlUtil.addTeamIdsConditions(conditionString, teamIds);
        conditionString = SqlUtil.addTeamIdsConditions(conditionString, pageRequestBean.getTeamConditions());
        List<Task> list = taskMapper.getPageAll(pageRequestBean.getPage() * pageRequestBean.getCount(), pageRequestBean.getCount(), conditionString, pageRequestBean.getOrderBy(), pageRequestBean.getSortType());
        List<TaskOutputVo> resultList = list.stream().map(task -> {
            TaskOutputVo vo = BeanUtil.copy(task, TaskOutputVo.class);
            QueryRange queryRange = JSON.parseObject(task.getArgs(), QueryRange.class);
            queryRange.setQuery(queryRange.getQuery().replaceAll("%s", ""));
            vo.setQueryRange(queryRange);
            CompletableFuture<Map<Long, Object>> idToNameUserMapAsync = getIdToNameMapAsync(task.getCreateUserId());
            CompletableFuture<Map<Long, String>> idToNameTeamMapAsync = getIdToNameMapAsync(task.getTeamIds());
//            CompletableFuture<Map<String, Set<String>>> conditionByMetricsNameAsync = getConditionByMetricsNameAsync(queryRange.getQuery().replaceAll("%s", ""), 1);
            // 拼装指标集
            Map<Long, Object> map = SqlUtil.map(task.getMetricsId(), kpiMapper.selectById(task.getMetricsId()).getName()).build();
            vo.setMetricsIdToNameMap(map);
            CompletableFuture.allOf(idToNameUserMapAsync, idToNameTeamMapAsync).join();
            try {
                // User
                vo.setCreateUserIdToNameMap(idToNameUserMapAsync.get());
                // Team
                vo.setTeamIdToNameMap(idToNameTeamMapAsync.get());
                // 拼装条件
//                Map<String, Set<String>> conditionByMetricsName = conditionByMetricsNameAsync.get();
//                if (CollectionUtil.isNotEmpty(conditionByMetricsName)) {
//                    vo.setConditionResult(conditionByMetricsName);
//                }
                return vo;
            } catch (Exception e) {
                System.out.println(vo);
            }
            return vo;
        }).collect(Collectors.toList());
        Integer pageAllSize = taskMapper.getPageAllSize(conditionString);
        return ResponseHelper.OK(resultList, pageAllSize);
    }

    @Async
    CompletableFuture<Map<String, Set<String>>> getConditionByMetricsNameAsync(String metricsName, Integer minute) {
        try {
            return CompletableFuture.completedFuture(getConditionByMetricsName(metricsName, minute));
        } catch (Exception e) {
        }
        return CompletableFuture.completedFuture(Collections.emptyMap());
    }

    @Async
    CompletableFuture<Map<Long, String>> getIdToNameMapAsync(String teamIds) {
        try {
            // 拼装Team
            if (!StringUtil.isEmpty(teamIds))
                return CompletableFuture.completedFuture(userClient.getIdToNameMap(teamIds).getData());
        } catch (Exception e) {

        }
        return CompletableFuture.completedFuture(Collections.emptyMap());
    }

    @Async
    CompletableFuture<Map<Long, Object>> getIdToNameMapAsync(Long userId) {
        if (userId != null && userId != 0) {
            R<User> rpcResult = userClient.userById(userId);
            if (rpcResult.isSuccess())
                return CompletableFuture.completedFuture(SqlUtil.map(userId, rpcResult.getData().getName()).build());
        }
        return CompletableFuture.completedFuture(Collections.emptyMap());
    }

    @Override
    public Map<String, Set<String>> getConditionByMetricsName(String metricsName, Integer minute) {
        Date now = new Date();
        // 取5分钟内的指标标签和对应的值
        Date date = DateUtil.setMinutes(now, -minute);
        // result结果list
        List<Map<String, Object>> maps = rangeQueryToList(metricsName, date, now, 60);
        // 标签与可取值list的map
        Map<String, Set<String>> conditionResult = new HashMap<>();
        maps.forEach(item -> {
            Map<String, String> metric = JSON.parseObject(item.get("metric").toString(), Map.class);
            // 遍历metric
            metric.forEach((key, value) -> {
                // 公共标签值，忽略
                if (PromBaseLables.baseLables.contains(key))
                    return;
                if (!conditionResult.containsKey(key)) {
                    HashSet<String> sets = new HashSet<>();
                    sets.add(value);
                    conditionResult.put(key, sets);
                } else {
                    Set<String> sets = conditionResult.get(key);
                    sets.add(value);
                    conditionResult.put(key, sets);
                }
            });
        });
        conditionResult.put("baseLables", Sets.newHashSet(PromBaseLables.baseLables));
        return conditionResult;
    }

    @Override
    public Map<String, MatrixData> getRangeValues(MetisDto metisDto, Date date, String metricName, Integer span, Integer step, Map<String, String> conditions) throws ExecutionException, InterruptedException {
        String realQuery = PromQLUtil.getQueryConditionStr(metricName, conditions);
        Date begin = DateUtil.setSeconds(date, -span);
        List<MatrixData> matrixData = rangeQuery(realQuery, begin, date, step);
        Map<String, MatrixData> realResultMap = matrixData.stream().collect(Collectors.toMap(MatrixData::specialKey, a -> a, (k1, k2) -> k1));
        return detectByMetis(date, realQuery, realResultMap, metisDto);
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
        System.out.println(url);
        URI uri = parseUrl(url);
        ResponseEntity<String> forEntity = restTemplate.getForEntity(uri, String.class);
        JSONObject jsonObject = verifyResponse(forEntity);
        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("result");
        List<VectorData> vectorData = JSON.parseArray(jsonArray.toJSONString(), VectorData.class);
        return vectorData;
    }

    @Override
    public List<MatrixData> rangeQuery(String query, Date start, Date end, Integer step) {
        JSONArray jsonArray = sendRangQuery(query, start, end, step);
        List<MatrixData> matrixData = JSON.parseArray(jsonArray.toJSONString(), MatrixData.class);
        return matrixData;
    }

    @Override
    public List<Map<String, Object>> rangeQueryToList(String query, Date start, Date end, Integer step) {
        JSONArray jsonArray = sendRangQuery(query, start, end, step);
        List<Map<String, Object>> list = (List) jsonArray;
        return list;
    }

    private JSONArray sendRangQuery(String query, Date start, Date end, Integer step) {
        String url = serverUrl + "query_range?query=" + query + "&start=" + parseTime(start) + "&end=" + parseTime(end) + "&step=" + step;
        System.out.println(url);
        URI uri = parseUrl(url);
        ResponseEntity<String> forEntity = restTemplate.getForEntity(uri, String.class);
        JSONObject jsonObject = verifyResponse(forEntity);
        JSONArray jsonArray = jsonObject.getJSONObject("data").getJSONArray("result");
        return jsonArray;
    }

    @Override
    public List<MetricsCsvVo> createMetisCsvVo(Date now, String realQuery, MetisDto metisDto) throws ExecutionException, InterruptedException {
        System.out.println("-----------当前时刻");
        Date date = DateUtil.setHours(now, -3);
        CompletableFuture<List<MatrixData>> metisDataAsyncA = getMetisDataFirst(realQuery, date, now);
        System.out.println("------------一天前");
        Date yesterday = DateUtil.setDays(now, -1);
        CompletableFuture<List<MatrixData>> metisDataAsyncB = getMetisDataFirst(realQuery, DateUtil.setHours(yesterday, -3), DateUtil.setHours(yesterday, 3));
        System.out.println("------------一周前");
        Date weekEarlier = DateUtil.setWeeks(now, -1);
        CompletableFuture<List<MatrixData>> metisDataAsyncC = getMetisDataFirst(realQuery, DateUtil.setHours(weekEarlier, -3), DateUtil.setHours(weekEarlier, 3));
        // 等待3者执行完成 多线程异步执行
        CompletableFuture.allOf(metisDataAsyncA, metisDataAsyncB, metisDataAsyncC).join();
        Map<String, MatrixData> metisDataA = metisDataAsyncA.get().stream().collect(Collectors.toMap(MatrixData::specialKey, a -> a, (k1, k2) -> k1));
        Map<String, MatrixData> metisDataB = metisDataAsyncB.get().stream().collect(Collectors.toMap(MatrixData::specialKey, a -> a, (k1, k2) -> k1));
        Map<String, MatrixData> metisDataC = metisDataAsyncC.get().stream().collect(Collectors.toMap(MatrixData::specialKey, a -> a, (k1, k2) -> k1));
        List<MetricsCsvVo> resultLists = new ArrayList<>();
        metisDataA.forEach((key, value) -> {
            MatrixData matrixDataA = metisDataA.get(key);
            MatrixData matrixDataB = metisDataB.get(key);
            MatrixData matrixDataC = metisDataC.get(key);
            if (matrixDataA == null || matrixDataB == null || matrixDataC == null)
                return;
            MetricsCsvVo metricsCsvVo = MetricsCsvVo.builder()
                    .viewId(metisDto.getViewId())
                    .viewName(metisDto.getViewName())
                    .attrName(metisDto.getAttrName())
                    .attrId(metisDto.getAttrId())
                    .dataA(matrixDataA.getMetisData())
                    .dataB(matrixDataB.getMetisData())
                    .dataC(matrixDataC.getMetisData())
                    .dateTime(now.getTime() / 1000)
                    .window("180")
                    .source(metisDto.getSource())
                    .trainOrTest(metisDto.getTrainOrTest())
                    .positiveOrNegative("positive")
                    .build();
            resultLists.add(metricsCsvVo);
        });
        return resultLists;
    }

    private Map<String, MatrixData> detectByMetis(Date now, String realQuery, Map<String, MatrixData> matrixDataMap, MetisDto metisDto) throws ExecutionException, InterruptedException {
        System.out.println("-----------当前时刻");
        Date date = DateUtil.setHours(now, -3);
        CompletableFuture<List<MatrixData>> metisDataAsyncA = getMetisDataFirst(realQuery, date, now);
        System.out.println("------------一天前");
        Date yesterday = DateUtil.setDays(now, -1);
        CompletableFuture<List<MatrixData>> metisDataAsyncB = getMetisDataFirst(realQuery, DateUtil.setHours(yesterday, -3), DateUtil.setHours(yesterday, 3));
        System.out.println("------------一周前");
        Date weekEarlier = DateUtil.setWeeks(now, -1);
        CompletableFuture<List<MatrixData>> metisDataAsyncC = getMetisDataFirst(realQuery, DateUtil.setHours(weekEarlier, -3), DateUtil.setHours(weekEarlier, 3));
        // 等待3者执行完成 多线程异步执行
        CompletableFuture.allOf(metisDataAsyncA, metisDataAsyncB, metisDataAsyncC).join();
        Map<String, MatrixData> metisDataA = metisDataAsyncA.get().stream().collect(Collectors.toMap(MatrixData::specialKey, a -> a, (k1, k2) -> k1));
        Map<String, MatrixData> metisDataB = metisDataAsyncB.get().stream().collect(Collectors.toMap(MatrixData::specialKey, a -> a, (k1, k2) -> k1));
        Map<String, MatrixData> metisDataC = metisDataAsyncC.get().stream().collect(Collectors.toMap(MatrixData::specialKey, a -> a, (k1, k2) -> k1));
        System.out.println(matrixDataMap.size() + " " + metisDataA.size() + " " + metisDataB.size() + " " + metisDataC.size());
        System.out.println("执行完毕");
        if (CollectionUtils.isEmpty(matrixDataMap))
            matrixDataMap = metisDataA;
        String viewName = "指标集名";
        String attrName = "指标名";
        String modelName = "1565079045604";
        String viewId = "2012";
        String attrId = "19201";
        if (metisDto != null) {
            viewId = metisDto.getViewId() + "";
            viewName = metisDto.getViewName();
            attrId = metisDto.getAttrId() + "";
            attrName = metisDto.getAttrName();
            modelName = metisDto.getModelName();
        }
        return getRealMetisResult(now, viewId, attrId, viewName, attrName, modelName, matrixDataMap, metisDataA, metisDataB, metisDataC);
    }

    private Map<String, MatrixData> getRealMetisResult(Date now, String viewId, String attrId, String viewName, String attrName, String modelName, Map<String, MatrixData> matrixDataMap, Map<String, MatrixData> metisDataA, Map<String, MatrixData> metisDataB, Map<String, MatrixData> metisDataC) {
        matrixDataMap.forEach((key, value) -> {
            MatrixData matrixDataA = metisDataA.get(key);
            MatrixData matrixDataB = metisDataB.get(key);
            MatrixData matrixDataC = metisDataC.get(key);
            if (matrixDataA != null && matrixDataB != null & matrixDataC != null) {
                Detector detector = Detector.builder()
                        .attrName(attrName)
                        .window(180)
                        .viewId(viewId)
                        .viewName(viewName)
                        .attrId(attrId)
                        .dataA(matrixDataA.getMetisData())
                        .dataB(matrixDataB.getMetisData())
                        .dataC(matrixDataC.getMetisData())
                        .taskId(modelName)
                        .time(metisDateFormat.format(now)).build();
                R<DetectorResult> rpcResult = detectorFeign.timeSeriesDetector(detector);
                if (rpcResult.isSuccess()) {
                    matrixDataMap.get(key).setDetectResult(rpcResult.getData().getData());
                }
            }
        });
        return matrixDataMap;
    }

    @Async
    CompletableFuture<List<MatrixData>> getMetisDataFirst(String query, Date begin, Date end) {
        List<MatrixData> matrixData = rangeQuery(query, begin, end, 60);
        matrixData.forEach(result -> {
            StringBuffer sb = new StringBuffer();
            result.getValues().forEach(item -> sb.append(getRealMetisValue(item)));
            result.setMetisData(sb.substring(0, sb.length() - 1));
        });
        return CompletableFuture.completedFuture(matrixData);
    }


    private String getRealMetisValue(String item) {
        String substring = item.substring(item.indexOf(",") + 2, item.indexOf("]") - 1);
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

    private URI parseUrl(String url) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        URI uri = builder.build().encode().toUri();
        return uri;
    }


    // 以下方法已废弃
    @Override
    public ResponseEntity<ResultBean> queryMetrics(Task task, Integer type) {
        if (Constant.NumberType.ZERO_PROPERTY.equals(type)) {
            QueryInstant queryInstant = null;
            return ResponseHelper.OK(instantQuery(queryInstant.getQuery(), queryInstant.getDate(), queryInstant.getTimeout()));
        } else if (Constant.NumberType.ONE_PROPERTY.equals(type)) {
            QueryRange queryRange = task.getQueryRange();
            List<MatrixData> matrixData = rangeQuery(queryRange.getQuery(), queryRange.getStart(), queryRange.getEnd(), queryRange.getStep());
            return ResponseHelper.OK(matrixData);
        }
        throw new ApiException(ApiResultEnum.FUNCTION_NOT_EXEC_ERROR);
    }


    @Override
    public ResponseEntity<ResultBean> exceptionDetect(QueryMetricProperty queryMetricProperty, Integer type) throws ExecutionException, InterruptedException {
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
        Map<String, MatrixData> realResult = detectByMetis(new Date(), realQuery, null, null);
        return ResponseHelper.OK(realResult);
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

}
