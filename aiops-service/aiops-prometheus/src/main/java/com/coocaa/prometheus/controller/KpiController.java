package com.coocaa.prometheus.controller;

import com.coocaa.common.request.*;
import com.coocaa.core.log.response.ResponseHelper;
import com.coocaa.core.log.response.ResultBean;
import com.coocaa.core.tool.utils.BeanUtil;
import com.coocaa.core.tool.utils.SqlUtil;
import com.coocaa.prometheus.entity.Kpi;
import com.coocaa.prometheus.mapper.KpiMapper;
import com.coocaa.prometheus.output.KpiOutputVo;
import com.coocaa.prometheus.output.TaskOutputVo;
import com.coocaa.prometheus.service.KpiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @description: 指标清单控制层
 * @author: dongyang_wu
 * @create: 2019-08-08 09:40
 */
@RestController
@RequestMapping("/kpiListing")
@Api(description = "指标集清单模块", tags = "指标集清单接口")
@AllArgsConstructor
public class KpiController {
    private KpiMapper kpiListingMapper;
    private KpiService kpiService;

    @PostMapping("/create")
    @ApiOperation(value = "新建或修改指标项")
    public ResponseEntity<ResultBean> create(@RequestBody Kpi kpi) {
        return ResponseHelper.OK(kpiService.create(kpi));

    }

    @PostMapping
    @ApiOperation(value = "分页获取指标清单列表")
    public ResponseEntity<ResultBean> getsKpiListing(@RequestBody PageRequestBean pageRequestBean) {
        RequestUtil.setDefaultPageBean(pageRequestBean);
        String conditionString = SqlUtil.getConditionString(pageRequestBean.getConditions(), pageRequestBean.getConditionConnection());
        List<Kpi> list = kpiListingMapper.getPageAll(pageRequestBean.getPage() * pageRequestBean.getCount(), pageRequestBean.getCount(), conditionString, pageRequestBean.getOrderBy(), pageRequestBean.getSortType());
        List<KpiOutputVo> resultList = list.stream().map(kpi -> {
            KpiOutputVo vo = BeanUtil.copy(kpi, KpiOutputVo.class);
            vo.setQueryPromExpression(kpi.getPromExpression().replaceAll("%s", ""));
            return vo;
        }).collect(Collectors.toList());
        Integer pageAllSize = kpiListingMapper.getPageAllSize(conditionString);
        return ResponseHelper.OK(resultList, pageAllSize);
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "批量删除指标清单")
    public ResponseEntity<ResultBean> deleteKpiListing(@RequestBody RequestBean requestBean) {
        kpiService.deletes(requestBean);
        return ResponseHelper.OK();
    }

}