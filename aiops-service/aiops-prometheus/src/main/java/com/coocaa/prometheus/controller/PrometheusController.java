package com.coocaa.prometheus.controller;

import com.coocaa.common.request.*;
import com.coocaa.core.log.exception.ApiException;
import com.coocaa.core.log.exception.ApiResultEnum;
import com.coocaa.core.log.response.ResponseHelper;
import com.coocaa.core.log.response.ResultBean;
import com.coocaa.prometheus.entity.PrometheusConfig;
import com.coocaa.prometheus.entity.Task;
import com.coocaa.prometheus.input.*;
import com.coocaa.prometheus.service.PromQLService;
import com.coocaa.prometheus.util.FileJsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.*;
import java.util.concurrent.ExecutionException;

/**
 * @description:
 * @author: dongyang_wu
 * @create: 2019-07-31 22:34
 */
@RestController
@RequestMapping("/prometheus")
@Api(description = "prometheus接口模块", tags = "prometheus接口")
@AllArgsConstructor
public class PrometheusController {
    private FileJsonUtil fileJsonUtil;
    private PromQLService promQLService;


    @PostMapping("/values")
    @ApiOperation(value = "根据指标名、筛选条件、时间间隔和步长获取具体时间端的数据同时判断dateTime时间点是否异常",
            notes = "metricsName: http_requests_total%s(%s不可省,为存放条件的位置)  \n" +
                    "dateTime: Unix时间戳(1565045729) \n" +
                    "span: 秒钟单位,距date多少秒钟的数据  \n" +
                    "step: 秒钟单位,步长  \n" +
                    "conditions: 条件的map(调用condition接口可以获取到可取的值)  \n" +
                    "返回有效值:  \n" +
                    "metric:对应指标名  \n" +
                    "values:相应时刻与对应的值  \n" +
                    "detectResult:指定date时刻异常检测结果  \n")
    public ResponseEntity<ResultBean> getValues(@RequestBody GetValuesInput getValuesInput) throws ExecutionException, InterruptedException {
        Date date;
        if (getValuesInput.getDateTime() == null) {
            date = new Date();
        } else {
            date = new Date(getValuesInput.getDateTime() * 1000);
        }
        return ResponseHelper.OK(promQLService.getRangeValues(null, date, getValuesInput.getMetricsName(), getValuesInput.getSpan(), getValuesInput.getStep(), getValuesInput.getConditions()));
    }

    @GetMapping("/targets")
    @ApiOperation("获取公司普罗米修斯监控项")
    public ResponseEntity<ResultBean> getTargets() {
        return promQLService.getTargets();
    }


    // 以下方法已废弃
    @PostMapping("/config/{type}/{mode}")
    @ApiOperation(value = "添加、修改或删除Prometheus需要监控的机器服务(instance不可重复)",
            notes = "instance:实例名;  \n" +
                    "targets:监控机器ip(例:39.108.106.167:8086);  \n" +
                    "type:0为设置指标路径为ip:port/metrics的服务-----1为设置ip:port/actuator/prometheus的服务;  \n" +
                    "mode为0新增或修改已存在的instance-----为1为删除指定instance的配置")
    @ApiIgnore
    public ResponseEntity<ResultBean> create(@RequestBody List<PrometheusConfig> prometheusConfigs, @PathVariable Integer type, @PathVariable Integer mode) {
        if (RequestUtil.isInValidParameter(0, 1, type, mode))
            throw new ApiException(ApiResultEnum.FUNCTION_PARAMETER_SCOPE_ERROR);
        return ResponseHelper.OK(fileJsonUtil.createJsonFile(prometheusConfigs, type, mode));
    }


    @PostMapping("/query/{type}")
    @ApiOperation(value = "查询范围或即时指定指标数据",
            notes = "task: task中只需传queryInstant或queryRange  \n" +
                    "query: 查询指标英文名(http请求数:http_requests_total,cpu一分钟负载:node_load1)  \n" +
                    "type: 0即时1范围  \n" +
                    "例：  \"queryRange\": {\n" +
                    "    \"query\": \"http_requests_total\",\n" +
                    "    \"start\": \"2019-08-02 7:03:43\",\n" +
                    "    \"end\": \"2019-08-02 10:03:43\",\n" +
                    "    \"step\": 60\n" +
                    "  }")
    @ApiIgnore
    public ResponseEntity<ResultBean> queryMetrics(@RequestBody Task task, @PathVariable Integer type) {
        return promQLService.queryMetrics(task, type);
    }

    @PostMapping("/detect/{type}")
    @ApiOperation(value = "带条件检测异常指标(以当前最新时间为准)",
            notes = "type: 查询指标类型(0http请求数1上传带宽2下载带宽3TCP连接数4CPU使用率5CPU负载6磁盘IO每秒花费时间7网络进带宽8内存cache)  \n" +
                    "instance: 查询条件具体机器名  \n" +
                    "0可选条件如下:  \n" +
                    "request: 可取apkupgrade,fetchad,getAdSettings,getAppCategorys,screensaver,timeline或otherwise  \n" +
                    "status:  http请求状态码  \n" +
                    "1、2无可选条件  \n" +
                    "3、4、6、7、8可选条件为(instance)  \n" +
                    "5可选条件为(instance)、metricsName必填(node_load1%s、node_load5%s或node_load15%s))  \n")
    @ApiIgnore
    public ResponseEntity<ResultBean> exceptionDetect(@RequestBody QueryMetricProperty queryMetricProperty, @PathVariable Integer type) throws ExecutionException, InterruptedException {
        return promQLService.exceptionDetect(queryMetricProperty, type);
    }
}