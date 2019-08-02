package com.coocaa.prometheus.controller;

import com.coocaa.common.request.RequestBean;
import com.coocaa.common.request.RequestUtil;
import com.coocaa.core.log.exception.ApiException;
import com.coocaa.core.log.exception.ApiResultEnum;
import com.coocaa.core.log.response.ResponseHelper;
import com.coocaa.core.log.response.ResultBean;
import com.coocaa.prometheus.entity.PrometheusConfig;
import com.coocaa.prometheus.entity.Task;
import com.coocaa.prometheus.input.QueryMetricProperty;
import com.coocaa.prometheus.service.PromQLService;
import com.coocaa.prometheus.service.TaskService;
import com.coocaa.prometheus.util.FileJsonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @program: intelligent_maintenance
 * @description:
 * @author: dongyang_wu
 * @create: 2019-07-31 22:34
 */
@RestController
@Api(value = "对接Prometheus接口模块", tags = "Prometheus接口")
@AllArgsConstructor
public class PrometheusController {
    private FileJsonUtil fileJsonUtil;
    private TaskService taskService;
    private PromQLService promQLService;

    @PostMapping("/config/{type}/{mode}")
    @ApiOperation(value = "添加、修改或删除Prometheus需要监控的机器服务(instance不可重复)",
            notes = "instance:实例名;  \n" +
                    "targets:监控机器ip(例:39.108.106.167:8086);  \n" +
                    "type:0为设置指标路径为ip:port/metrics的服务-----1为设置ip:port/actuator/prometheus的服务;  \n" +
                    "mode为0新增或修改已存在的instance-----为1为删除指定instance的配置")
    public ResponseEntity<ResultBean> create(@RequestBody List<PrometheusConfig> prometheusConfigs, @PathVariable Integer type, @PathVariable Integer mode) {
        if (RequestUtil.isInValidParameter(0, 1, type, mode))
            throw new ApiException(ApiResultEnum.FUNCTION_PARAMETER_SCOPE_ERROR);
        return ResponseHelper.OK(fileJsonUtil.createJsonFile(prometheusConfigs, type, mode));
    }

    @PostMapping("/create/{type}")
    @ApiOperation(value = "新建或更新监控定时任务(会重新启动定时任务可用于更改时间粒度)",
            notes = "task例:  \n" +
                    "   taskId: 16,  \n" +
                    "  taskName: 业务访问量定时任务,  \n" +
                    "  taskDescription: 业务访问量定时任务,  \n" +
                    "  taskCron:  0/17 * * * * ?, \n" +
                    "  queryMetric: http请求量,  \n" +
                    "  queryInstant: {   \n" +
                    "  query: http_requests_total,  \n" +
                    "  timeout: 1000  \n" +
                    "  }   \n" +
                    "type:0即时数据传queryInstant 1范围数据传queryRange;  \n")
    public ResponseEntity<ResultBean> createTask(@RequestBody Task task, @PathVariable Integer type) {
        Boolean queryMetricsTask = taskService.createQueryMetricsTask(task, type);
        return ResponseHelper.OK(queryMetricsTask);
    }

    @PostMapping("/stop/{type}")
    @ApiOperation(value = "删除、停止或禁用监控定时任务",
            notes = "query: 对应数据库表键值如id  \n" +
                    "queryString: 对应查询值如17  \n" +
                    "type: 0删除1停止2禁用  \n")
    public ResponseEntity<ResultBean> stopTask(@RequestBody RequestBean requestBean, @PathVariable Integer type) {
        Boolean queryMetricsTask = taskService.removeQueryMetricsTask(requestBean, type);
        return ResponseHelper.OK(queryMetricsTask);
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
    public ResponseEntity<ResultBean> exceptionDetect(@RequestBody QueryMetricProperty queryMetricProperty, @PathVariable Integer type) {
        return promQLService.exceptionDetect(queryMetricProperty, type);
    }

    @GetMapping("/targets")
    @ApiOperation("获取公司普罗米修斯监控项")
    public ResponseEntity<ResultBean> getTargets() {
        return promQLService.getTargets();
    }
}