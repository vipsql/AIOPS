/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.coocaa.core.log.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coocaa.core.log.model.LogError;
import com.coocaa.core.log.model.LogErrorVo;
import com.coocaa.core.log.service.ILogErrorService;
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
@RequestMapping("/error")
public class LogErrorController {

	private ILogErrorService errorLogService;

	/**
	 * 查询单条
	 */
	@GetMapping("/detail")
	public R<LogError> detail(LogError logError) {
		return R.data(errorLogService.getOne(Condition.getQueryWrapper(logError)));
	}

	/**
	 * 查询多条(分页)
	 */
	@GetMapping("/list")
	public R<IPage<LogErrorVo>> list(@ApiIgnore @RequestParam Map<String, Object> logError, Query query) {
		IPage<LogError> pages = errorLogService.page(Condition.getPage(query.setDescs("create_time")), Condition.getQueryWrapper(logError, LogError.class));
		List<LogErrorVo> records = pages.getRecords().stream().map(logApi -> {
			LogErrorVo vo = BeanUtil.copy(logApi, LogErrorVo.class);
			vo.setStrId(Func.toStr(logApi.getId()));
			return vo;
		}).collect(Collectors.toList());
		IPage<LogErrorVo> pageVo = new Page<>(pages.getCurrent(), pages.getSize(), pages.getTotal());
		pageVo.setRecords(records);
		return R.data(pageVo);
	}

}
