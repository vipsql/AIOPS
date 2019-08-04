package com.coocaa.auth.service.impl;

import com.alibaba.fastjson.JSON;
import com.coocaa.auth.service.AuthService;
import com.coocaa.common.constant.Constant;
import com.coocaa.common.constant.RedisConstant;
import com.coocaa.core.boot.redis.RedisUtil;
import com.coocaa.core.log.exception.ApiException;
import com.coocaa.core.log.exception.ApiResultEnum;
import com.coocaa.core.secure.constant.TokenConstant;
import com.coocaa.core.secure.utils.LdapUtil;
import com.coocaa.core.secure.utils.SecureUtil;
import com.coocaa.core.log.response.ResponseHelper;
import com.coocaa.core.log.response.ResultBean;
import com.coocaa.core.tool.api.R;
import com.coocaa.core.tool.utils.*;
import com.coocaa.user.entity.User;
import com.coocaa.user.entity.UserInfo;
import com.coocaa.user.feign.IUserClient;
import com.novell.ldap.LDAPEntry;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: intelligent_maintenance
 * @description:
 * @author: dongyang_wu
 * @create: 2019-07-29 10:19
 */
@Service("AuthService")
@CacheConfig(cacheNames = RedisConstant.AUTH_TOKEN.PREFIX)
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private IUserClient userClient;
    private LdapUtil ldapUtil;
    private RedisUtil redisUtil;

    @Override
    public ResponseEntity<ResultBean> token(String account, String password) {
        R<UserInfo> res = userClient.userInfo(account, DigestUtil.encrypt(password));
        User user = res.getData().getUser();
        // 验证用户
        if (Func.isEmpty(user.getId())) {
            return ResponseHelper.BadRequest("用户名或密码不正确");
        }
        String accessToken = getAuthInfoFromUser(user);
        return getHeaderResult(accessToken, user);
    }

    @Override
    public ResponseEntity<ResultBean> tokenByLdap(String account, String password) {
        // 判断缓存中是否存在
        Object o = redisUtil.get(String.format(RedisConstant.AUTH_TOKEN.TOKEN_PREFIX, account));
        if (!ObjectUtil.isEmpty(o)) {
            String[] userAndToken = o.toString().split(" ");
            String token = userAndToken[0];
            User user = JSON.parseObject(userAndToken[1], User.class);
            return getHeaderResult(token, user);
        }
        R<User> apiResult = userClient.userByMail(account);
        String token = "";
        User user;
        if (!R.isSuccess(apiResult)) {
            LDAPEntry userInfo = ldapUtil.validAndGetUser("zhanghonghao@coocaa.com", "xxx");
            String[] userInfos = userInfo.getDN().split(",");
            String name = userInfo.getAttributeSet().getAttribute("name").getStringValue();
            user = User.builder()
                    .name(name)
                    .account(name)
                    .mail(userInfo.getAttributeSet().getAttribute("mail").getStringValue())
                    .mobile(userInfo.getAttributeSet().getAttribute("mobile").getStringValue())
                    .company(userInfos[4].split("=")[1])
                    .department(userInfos[3].split("=")[1])
                    .departmentGroup(userInfos[2].split("=")[1])
                    .organization(userInfos[1].split("=")[1])
                    .password(password)
                    .build();
            R<Integer> insert = userClient.insert(user);
            if (R.isSuccess(insert)) {
                token = getAuthInfoFromUser(user);
            }
        } else {
            user = apiResult.getData();
            token = getAuthInfoFromUser(user);
        }
        if (user != null && !StringUtils.isEmpty(token)) {
            // 加入缓存
            redisUtil.set(String.format(RedisConstant.AUTH_TOKEN.TOKEN_PREFIX, account), token + " " + JSON.toJSONString(user), RedisConstant.Redis.EXPIRE_TIME_FIVE_MINUTE);
            return getHeaderResult(token, user);
        }
        throw new ApiException(ApiResultEnum.FUNCTION_NOT_EXEC_ERROR);
    }

    @Override
    @Cacheable(key = "#a")
    public String testCache(String a) {
        System.out.println(a);
        return "测试";
    }

    private String getAuthInfoFromUser(User user) {
        //设置jwt参数
        Map<String, String> param = new HashMap<>(16);
        param.put(TokenConstant.USER_ID, Func.toStr(user.getId()));
//        param.put(TokenConstant.ROLE_ID, user.get());
        param.put(TokenConstant.ACCOUNT, user.getAccount());
        param.put(TokenConstant.USER_NAME, user.getName());
//        param.put(TokenConstant.ROLE_NAME, Func.join(res.getData().getRoles()));
        //拼装accessToken
        String accessToken = SecureUtil.createJWT(param, "audience", "issuser", true);
        return accessToken;
    }

    private ResponseEntity<ResultBean> getHeaderResult(String accessToken, User user) {
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set(TokenConstant.HEADER, accessToken);
        responseHeaders.set("Access-Control-Expose-Headers", TokenConstant.HEADER);
        return new ResponseEntity<>(ResultBean.success(user), responseHeaders, HttpStatus.OK);
    }
}