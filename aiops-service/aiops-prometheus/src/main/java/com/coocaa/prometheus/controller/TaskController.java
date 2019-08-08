package com.coocaa.prometheus.controller;

import com.coocaa.common.constant.Constant;
import com.coocaa.common.request.*;
import com.coocaa.core.log.annotation.ApiLog;
import com.coocaa.core.log.response.ResponseHelper;
import com.coocaa.core.log.response.ResultBean;
import com.coocaa.detector.feign.IDetectorClient;
import com.coocaa.prometheus.input.MetisCsvInputVo;
import com.coocaa.prometheus.input.TaskInputVo;
import com.coocaa.prometheus.mapper.TaskMapper;
import com.coocaa.prometheus.output.MetisCsvOutputVo;
import com.coocaa.prometheus.output.MetricsCsvVo;
import com.coocaa.prometheus.service.*;
import com.coocaa.prometheus.util.PoiUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.*;

/**
 * @description: 指标控制器
 * @author: dongyang_wu
 * @create: 2019-08-05 15:14
 */
@RestController
@RequestMapping("/metrics")
@Api(description = "指标(定时任务)模块", tags = "指标(定时任务)接口")
@AllArgsConstructor
public class TaskController {
    private PromQLService promQLService;
    private TaskService taskService;

    @PostMapping
    @ApiOperation(value = "定时任务分页列表")
    @ApiLog("定时任务分页列表")
    public ResponseEntity<ResultBean> gets(@RequestBody PageRequestBean pageRequestBean) {
        return promQLService.listByPage(pageRequestBean);
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "批量删除定时任务")
    public ResponseEntity<ResultBean> delete(@RequestBody RequestBean requestBean) {
        taskService.deletes(requestBean);
        return ResponseHelper.OK();
    }

    @GetMapping("/condition/{minute}")
    @ApiOperation(value = "获取某个指标的标签及可取的值",
            notes = "例:  \n" +
                    "metricsName:http_requests_total  \n" +
                    "minute:分钟单位,多少分钟内的标签及其取值  \n")
    public ResponseEntity<ResultBean> getConditionByMetricsName(@RequestParam String metricsName, @PathVariable Integer minute) {
        return promQLService.getConditionByMetricsName(metricsName, minute);
    }

    @PostMapping("/create/{type}")
    @ApiOperation(value = "新建或更新监控定时任务(会重新启动定时任务可用于更改时间粒度)",
            notes = "type: 0不启动定时任务1启动" +
                    "{\n" +
                    "  \"id\": 0,\n" +
                    "  \"taskName\": \"指标实例-192.168.108.6\",\n" +
                    "  \"taskDescription\": \"内存Cache\",\n" +
                    "  \"taskCron\": \"1 0/2 * * * ?\",\n" +
                    "  \"modelName\": \"1565079045604\",\n" +
                    "  \"metricsId\": 1,\n" +
                    "  \"teamIds\": \"1\",\n" +
                    "  \"queryRange\": {\n" +
                    "    \"query\": \"node_load15%s\",\n" +
                    "    \"span\": 86400,\n" +
                    "    \"step\": 60,\n" +
                    "    \"conditions\": {\n" +
                    "      \"instance\": \"172.16.33.2:9100\"\n" +
                    "    }\n" +
                    "  }\n" +
                    "}")
    public ResponseEntity<ResultBean> createTask(@RequestBody TaskInputVo task, @PathVariable Integer type) {
        return ResponseHelper.OK(taskService.createQueryMetricsTask(task, type));
    }

    @PostMapping("/restart")
    @ApiOperation("重新启动定时任务")
    public ResponseEntity<ResultBean> restartTask(@RequestBody RequestBean requestbean) {
        taskService.restartTask(requestbean);
        return ResponseHelper.OK();
    }

    @DeleteMapping("/stop/{type}")
    @ApiOperation(value = "删除、停止或禁用监控定时任务",
            notes = "query: 对应数据库表键值如id  \n" +
                    "queryString: 对应查询值如17  \n" +
                    "type: 0删除1停止2禁用  \n")
    public ResponseEntity<ResultBean> stopTask(@RequestBody RequestBean requestBean, @PathVariable Integer type) {
        Boolean queryMetricsTask = taskService.removeQueryMetricsTask(requestBean, type);
        return ResponseHelper.OK(queryMetricsTask);
    }

    @PostMapping("/export")
    @ApiOperation(value = "导出指定时间段指定时间跨度的Metis训练数据",
            notes = "{\n" +
                    "  \"begin\": \"2019-08-08 03:19:12\",\n" +
                    "  \"end\": \"2019-08-08 03:19:12\",\n" +
                    "  \"span\": 86400,\n" +
                    "  \"taskId\": 55\n" +
                    "}")
    public void exportMetisCsv(@RequestBody MetisCsvInputVo metisCsvInputVo, @ApiIgnore HttpServletRequest request, @ApiIgnore HttpServletResponse response) throws Exception {
        List<MetricsCsvVo> metricsCsvVos = taskService.exportMetisCsv(metisCsvInputVo);
        PoiUtil.responseSetProperties("测试", request, response);
        OutputStream os = response.getOutputStream();
        PoiUtil.exportData2Csv(metricsCsvVos, Constant.MetisCsv.columns, Constant.MetisCsv.columnCns, os);
    }

    @PostMapping("/mark")
    @ApiOperation(value = "导出指定时间段指定时间跨度的Metis训练数据并进行人工标注")
    public ResponseEntity<ResultBean> exportMetisCsvToMark(@RequestBody MetisCsvInputVo metisCsvInputVo) throws Exception {
        MetisCsvOutputVo metisCsvOutputVo = taskService.exportMetisCsvToMark(metisCsvInputVo);
        return ResponseHelper.OK(metisCsvOutputVo);
    }

    @PostMapping("/train/{modelName}")
    @ApiOperation(value = "将人工标注好的数据传入Metis进行训练")
    public ResponseEntity<ResultBean> exportMetisCsvToTrain(@RequestBody MetisCsvOutputVo trainVos, @PathVariable String modelName) {
        taskService.exportMetisCsvToTrain(trainVos, modelName);
        return ResponseHelper.OK();

    }
}