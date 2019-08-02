package com.coocaa.core.tool.utils.http;

import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @auther: dongyang_wu
 * @date: 2019/4/1 15:31
 * @description:
 */
@Component("HttpHelper")
public class HttpHelper {
	private final String APPID = "wx2e7699073ea66d7a";
	private final String SECRET = "493f0fa4e3df8812c7ca82cb9e156dd1";

	public ResponseEntity get(String url, Map headers, Map<String, Object> paramMapArgs) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders httpHeaders = new HttpHeaders();
		if (headers != null) {
			httpHeaders.setAll(headers);
		}
		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, httpHeaders);
		ResponseEntity result;
		if (paramMapArgs != null)
			result = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class, paramMapArgs);
		else
			result = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
		return result;
	}

	public ResponseEntity post(String url, Map headers, Map<String, Object> bodyMapArgs) {
		HttpHeaders httpHeaders = new HttpHeaders();
		RestTemplate restTemplate = new RestTemplate();
		if (headers != null) {
			httpHeaders.setAll(headers);
		}
		MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
		bodyMap.setAll(bodyMapArgs);
		HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(bodyMap, httpHeaders);
		ResponseEntity result = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Map.class);
		return result;
	}

	public ResponseEntity wxLogin(String code) {
		String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + APPID + "&secret=" + SECRET + "&grant_type=authorization_code&js_code=" + code;
		ResponseEntity responseEntity = get(url, null, null);
		System.out.println(responseEntity.getBody().toString());
		return responseEntity;
	}
}
