package com.coocaa.core.log.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coocaa.core.boot.redis.RedisUtil;
import com.coocaa.core.log.model.LogApi;
import com.coocaa.core.log.model.LogApiVo;
import com.coocaa.core.log.service.ILogApiService;
import com.coocaa.core.mybatis.support.Condition;
import com.coocaa.core.mybatis.support.Query;
import com.coocaa.core.tool.api.R;
import com.coocaa.core.tool.utils.BeanUtil;
import com.coocaa.core.tool.utils.Func;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 控制器
 *
 * @author dongyang_wu
 */
@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class LogApiController {

	private ILogApiService logService;
	/**
	 * 查询单条
	 */
	@GetMapping("/detail")
	public R<LogApi> detail(LogApi log) {
		return R.data(logService.getOne(Condition.getQueryWrapper(log)));
	}

	/**
	 * 查询多条(分页)
	 */
	@GetMapping("/list")
	public R<IPage<LogApiVo>> list(@ApiIgnore @RequestParam Map<String, Object> log, Query query) {
		IPage<LogApi> pages = logService.page(Condition.getPage(query.setDescs("create_time")), Condition.getQueryWrapper(log, LogApi.class));
		List<LogApiVo> records = pages.getRecords().stream().map(logApi -> {
			LogApiVo vo = BeanUtil.copy(logApi, LogApiVo.class);
			vo.setStrId(Func.toStr(logApi.getId()));
			return vo;
		}).collect(Collectors.toList());
		IPage<LogApiVo> pageVo = new Page<>(pages.getCurrent(), pages.getSize(), pages.getTotal());
		pageVo.setRecords(records);
		return R.data(pageVo);
	}

}
