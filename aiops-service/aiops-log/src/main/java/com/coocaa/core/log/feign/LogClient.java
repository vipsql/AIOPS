package com.coocaa.core.log.feign;

import com.coocaa.core.log.model.*;
import com.coocaa.core.log.service.*;
import com.coocaa.core.tool.api.R;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;


/**
 * 日志服务Feign实现类
 *
 * @author dongyang_wu
 */
@RestController
@AllArgsConstructor
public class LogClient implements ILogClient {

    ILogUsualService usualLogService;

    ILogApiService apiLogService;
    ILogErrorService errorLogService;

    @Override
    @PostMapping(API_PREFIX + "/saveUsualLog")
    public R<Boolean> saveUsualLog(@RequestBody LogUsual log) {
        log.setParams(log.getParams().replace("&amp;", "&"));
        return R.data(usualLogService.save(log));
    }

    @Override
    @PostMapping(API_PREFIX + "/saveApiLog")
    public R<Boolean> saveApiLog(@RequestBody LogApi log) {
        log.setParams(log.getParams().replace("&amp;", "&"));
        apiLogService.deleteLogs();
        return R.data(apiLogService.save(log));
    }

    @Override
    @PostMapping(API_PREFIX + "/saveErrorLog")
    public R<Boolean> saveErrorLog(@RequestBody LogError log) {
        log.setParams(log.getParams().replace("&amp;", "&"));
        return R.data(errorLogService.save(log));
    }
}
