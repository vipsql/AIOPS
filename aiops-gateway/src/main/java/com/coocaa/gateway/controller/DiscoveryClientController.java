package com.coocaa.gateway.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @description: 服务发现控制器
 * @author: dongyang_wu
 * @create: 2019-07-28 21:25
 */
@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/discovery")
public class DiscoveryClientController {

	private final DiscoveryClient discoveryClient;

	/**
	 * 获取服务实例
	 */
	@GetMapping("/instances")
	public Map<String, List<ServiceInstance>> instances() {
		Map<String, List<ServiceInstance>> instances = new HashMap<>(16);
		List<String> services = discoveryClient.getServices();
		services.forEach(s -> {
			List<ServiceInstance> list = discoveryClient.getInstances(s);
			instances.put(s, list);
		});
		return instances;
	}

}
