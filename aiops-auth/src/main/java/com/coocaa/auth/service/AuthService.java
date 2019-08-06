package com.coocaa.auth.service;

import com.coocaa.core.log.response.ResultBean;
import org.springframework.http.ResponseEntity;

/**
 * @program: intelligent_maintenance
 * @description:
 * @author: dongyang_wu
 * @create: 2019-07-29 10:19
 */
public interface AuthService {
    ResponseEntity<ResultBean> token(String account, String password);

    ResponseEntity<ResultBean> tokenByLdap(String account, String password);

    String testCache(String a);
}