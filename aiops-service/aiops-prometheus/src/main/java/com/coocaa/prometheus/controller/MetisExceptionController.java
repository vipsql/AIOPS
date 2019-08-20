package com.coocaa.prometheus.controller;

import com.coocaa.common.constant.Constant;
import com.coocaa.common.request.*;
import com.coocaa.core.log.exception.ApiException;
import com.coocaa.core.log.exception.ApiResultEnum;
import com.coocaa.core.log.response.ResponseHelper;
import com.coocaa.core.log.response.ResultBean;
import com.coocaa.prometheus.input.MetisExceptionInputVo;
import com.coocaa.prometheus.service.MetisExceptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * @description: MetisException控制层
 * @author: dongyang_wu
 * @create: 2019-08-07 10:03
 */
@RestController
@RequestMapping("/metisexception")
@Api(description = "指标异常模块", tags = "指标异常接口")
@AllArgsConstructor
public class MetisExceptionController {
    private MetisExceptionService metisExceptionService;
    @PostMapping
    @ApiOperation(value = "分页获取指标异常列表",
            notes = "(以task_id为条件可以查询指定指标的历史异常记录)")
    public ResponseEntity<ResultBean> gets(@RequestBody PageRequestBean pageRequestBean) {
        return metisExceptionService.listByPage(pageRequestBean);
    }

    @DeleteMapping("/delete")
    @ApiOperation(value = "批量删除")
    public ResponseEntity<ResultBean> delete(@RequestBody RequestBean requestBean) {
        metisExceptionService.deletes(requestBean);
        return ResponseHelper.OK();
    }

    @PostMapping("/update")
    @ApiOperation("修改异常状态及上传修改理由")
    public ResponseEntity<ResultBean> update(@RequestBody MetisExceptionInputVo metisExceptionInputVo) {
        if (RequestUtil.isInValidParameter(Constant.MetisExceptionStatus.UNHANDLED, Constant.MetisExceptionStatus.HANDLED, metisExceptionInputVo.getStatus()))
            throw new ApiException(ApiResultEnum.FUNCTION_PARAMETER_SCOPE_ERROR);
        metisExceptionService.update(metisExceptionInputVo);
        return ResponseHelper.OK();
    }

}