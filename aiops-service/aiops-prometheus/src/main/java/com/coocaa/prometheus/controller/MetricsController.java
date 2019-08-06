package com.coocaa.prometheus.controller;

import com.alibaba.fastjson.JSONObject;
import com.coocaa.common.constant.Constant;
import com.coocaa.common.request.PageRequestBean;
import com.coocaa.common.request.RequestBean;
import com.coocaa.core.log.response.ResponseHelper;
import com.coocaa.core.log.response.ResultBean;
import com.coocaa.core.tool.utils.SqlUtil;
import com.coocaa.prometheus.entity.Metrics;
import com.coocaa.prometheus.input.MetisCsvInputVo;
import com.coocaa.prometheus.input.MetricsInputVo;
import com.coocaa.prometheus.mapper.MetricsMapper;
import com.coocaa.prometheus.output.MetricsCsvVo;
import com.coocaa.prometheus.service.MetricsService;
import com.coocaa.prometheus.service.PromQLService;
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
import java.util.List;

/**
 * @description: 指标控制器
 * @author: dongyang_wu
 * @create: 2019-08-05 15:14
 */
@RestController
@RequestMapping("/metrics")
@Api(value = "指标模块", tags = "Prometheus指标接口")
@AllArgsConstructor
public class MetricsController {
    private PromQLService promQLService;
    private MetricsService metricsService;
    private MetricsMapper metricsMapper;

    @GetMapping("/condition/{minute}")
    @ApiOperation(value = "获取某个指标的标签及可取的值",
            notes = "例:  \n" +
                    "metricsName:http_requests_total  \n" +
                    "minute:分钟单位,多少分钟内的标签及其取值  \n")
    public ResponseEntity<ResultBean> getConditionByMetricsName(@RequestParam String metricsName, @PathVariable Integer minute) {
        return promQLService.getConditionByMetricsName(metricsName, minute);
    }

    @PostMapping("/create/{type}")
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

    @DeleteMapping("/{id}/{type}")
    @ApiOperation(value = "删除指定id的指标",
            notes = "对指标对应的定时任务处理类型:  \n" +
                    "type: 0删除1停止2禁用  \n")
    @ApiIgnore
    public ResponseEntity<ResultBean> delete(@PathVariable Long id, @PathVariable Integer type) {
        return ResponseHelper.OK(metricsService.deleteMetrics(id, type));
    }

    @PostMapping("/delete")
    @ApiOperation(value = "批量删除")
    public ResponseEntity<ResultBean> delete(@RequestBody RequestBean requestBean) {
        metricsService.deletes(requestBean);
        return ResponseHelper.OK();
    }

    @PostMapping("/")
    @ApiOperation(value = "分页获取指标列表")
    public ResponseEntity<ResultBean> gets(@RequestBody PageRequestBean pageRequestBean) {
        String conditionString = SqlUtil.getConditionString(pageRequestBean.getConditions(), pageRequestBean.getConditionConnection());
        List<Metrics> list = metricsMapper.getPageAll(pageRequestBean.getPage() * pageRequestBean.getCount(), pageRequestBean.getCount(), conditionString);
        Integer pageAllSize = metricsMapper.getPageAllSize(conditionString);
        return ResponseHelper.OK(list, pageAllSize);
    }

    @GetMapping("/")
    @ApiOperation(value = "获取指标清单")
    public ResponseEntity<ResultBean> gets() {
        // 编写指标清单
        return ResponseHelper.OK();
    }

    @PostMapping("/export")
    @ApiOperation(value = "导出指定时间段指定时间跨度的Metis训练数据")
    public void exportMetisCsv(@RequestBody MetisCsvInputVo metisCsvInputVo, @ApiIgnore HttpServletRequest request, @ApiIgnore HttpServletResponse response) throws Exception {
        List<MetricsCsvVo> metricsCsvVos = metricsService.exportMetisCsv(metisCsvInputVo);
        PoiUtil.responseSetProperties("测试", request, response);
        OutputStream os = response.getOutputStream();
        PoiUtil.exportData2Csv(metricsCsvVos, Constant.MetisCsv.columns, Constant.MetisCsv.columnCns, os);
    }

    @PostMapping("/train")
    @ApiOperation(value = "导出指定时间段指定时间跨度的Metis训练数据并传入metis进行训练")
    public ResponseEntity<ResultBean> exportMetisCsvToTrain(@RequestBody MetisCsvInputVo metisCsvInputVo) throws Exception {
        JSONObject jsonObject = metricsService.exportMetisCsvToTrain(metisCsvInputVo);
        return ResponseHelper.OK(jsonObject);
    }
}