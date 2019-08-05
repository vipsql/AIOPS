package com.coocaa.prometheus.controller;

import com.coocaa.common.request.RequestBean;
import com.coocaa.core.log.response.ResponseHelper;
import com.coocaa.core.log.response.ResultBean;
import com.coocaa.prometheus.entity.Metrics;
import com.coocaa.prometheus.input.MetricsInputVo;
import com.coocaa.prometheus.mapper.MetricsMapper;
import com.coocaa.prometheus.service.MetricsService;
import com.coocaa.prometheus.service.PromQLService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @description: 指标控制器
 * @author: dongyang_wu
 * @create: 2019-08-05 15:14
 */
@RestController
@Api(value = "指标模块", tags = "Prometheus指标接口")
@AllArgsConstructor
public class MetricsController {
    private PromQLService promQLService;
    private MetricsService metricsService;
    private MetricsMapper metricsMapper;

    @GetMapping("/metrics/condition/{minute}")
    @ApiOperation(value = "获取某个指标的标签及可取的值",
            notes = "例:  \n" +
                    "metricsName:http_requests_total  \n" +
                    "minute:分钟单位,多少分钟内的标签及其取值  \n")
    public ResponseEntity<ResultBean> getConditionByMetricsName(@RequestParam String metricsName, @PathVariable Integer minute) {
        return promQLService.getConditionByMetricsName(metricsName, minute);
    }

    @PostMapping("/metrics/create/{type}")
    @ApiOperation(value = "新建或修改指标",
            notes = "新建时-----type:0不启动定时任务,1启动定时任务  \n" +
                    "修改时-----type:0不重新启动定时任务,1重新启动定时任务  \n" +
                    "例：          " +
                    "           {\"id\": 1,\n" +
                    "            \"metricName\": \"http请求数\",\n" +
                    "            \"teamIds\": \"11 11\",\n" +
                    "            \"modelIds\": \"11 13\",\n" +
                    "            \"taskCron\": \"0/17 * * * * ?\",\n" +
                    "            \"taskId\": 32,\n" +
                    "            \"queryRange\": {\n" +
                    "            \"query\": \"http_requests_total%s\",\n" +
                    "            \"span\": 86400,\n" +
                    "            \"step\": 60,\n" +
                    "            \"conditions\": {\n" +
                    "            \"instance\": \"172.16.20.143:3903\"\n" +
                    "             }   \n")
    public ResponseEntity<ResultBean> createMetrics(@PathVariable Integer type, @RequestBody MetricsInputVo metricsInputVo) {
        return ResponseHelper.OK(metricsService.createMetrics(type, metricsInputVo));
    }

    @DeleteMapping("/metrics/{id}/{type}")
    @ApiOperation(value = "删除指定id的指标",
            notes = "对指标对应的定时任务处理类型:  \n" +
                    "type: 0删除1停止2禁用  \n")
    public ResponseEntity<ResultBean> delete(@PathVariable Long id, @PathVariable Integer type) {
        return ResponseHelper.OK(metricsService.deleteMetrics(id, type));
    }

    @GetMapping("/metrics")
    @ApiOperation(value = "获取指标清单")
    public ResponseEntity<ResultBean> gets() {
        // 编写指标清单
        return ResponseHelper.OK();
    }

    @PostMapping("/metrics/attr/{page}/{count}")
    @ApiOperation(value = "获取符合属性指标列表")
    public ResponseEntity<ResultBean> gets(@RequestBody RequestBean requestBean, @PathVariable Integer page, @PathVariable Integer count) {
        // 获取指标
        return ResponseHelper.OK();
    }

    @PostMapping("/metrics/{page}/{count}")
    @ApiOperation(value = "获取指标列表")
    public ResponseEntity<ResultBean> gets(@PathVariable Integer page, @PathVariable Integer count) {
        // 获取指标
        return ResponseHelper.OK();
    }
}