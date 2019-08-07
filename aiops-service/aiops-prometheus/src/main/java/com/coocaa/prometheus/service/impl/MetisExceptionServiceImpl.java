package com.coocaa.prometheus.service.impl;

import com.alibaba.fastjson.JSON;
import com.coocaa.core.log.exception.ApiException;
import com.coocaa.core.log.exception.ApiResultEnum;
import com.coocaa.core.mybatis.base.BaseServiceImpl;
import com.coocaa.core.secure.AiOpsUser;
import com.coocaa.core.secure.utils.SecureUtil;
import com.coocaa.core.tool.utils.MapUtil;
import com.coocaa.prometheus.entity.MetisException;
import com.coocaa.prometheus.input.MetisExceptionInputVo;
import com.coocaa.prometheus.mapper.MetisExceptionMapper;
import com.coocaa.prometheus.service.MetisExceptionService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: MetisException服务实现类
 * @author: dongyang_wu
 * @create: 2019-08-07 10:02
 */
@Service
@AllArgsConstructor
public class MetisExceptionServiceImpl extends BaseServiceImpl<MetisExceptionMapper, MetisException> implements MetisExceptionService {
    private MetisExceptionMapper metisExceptionMapper;

    @Override
    public void update(MetisExceptionInputVo metisExceptionInputVo) {
        MetisException metisException = metisExceptionMapper.selectById(metisExceptionInputVo.getId());
        if (metisException == null)
            throw new ApiException(ApiResultEnum.ENTITY_NOT_EXIST);
        metisException.setStatus(metisExceptionInputVo.getStatus());
        String userToReasonJson = metisException.getUserToReasonJson();
        AiOpsUser user = SecureUtil.getUser();
        metisException.setRecentUserId(user.getUserId());
        String userName = user.getUserName();
        ConcurrentHashMap<String, List<String>> userToReasonMap;
        // 不含JSON
        if (StringUtils.isEmpty(userToReasonJson)) {
            userToReasonMap = new ConcurrentHashMap<>();
            userToReasonMap.put(userName, Arrays.asList(metisExceptionInputVo.getReason()));
        }
        // 含JSON
        else {
            userToReasonMap = JSON.parseObject(userToReasonJson, (Type) ConcurrentHashMap.class);
            // 含相应用户的key
            if (userToReasonMap.containsKey(userName)) {
                List<String> userReasons = userToReasonMap.get(userName);
                userReasons.add(metisExceptionInputVo.getReason());
                if (userReasons.size() > 10) {
                    userReasons.remove(0);
                }
                userToReasonMap.put(userName, userReasons);
            }
            // 不含相应用户的key
            else {
                userToReasonMap.put(userName, Arrays.asList(metisExceptionInputVo.getReason()));
            }

            // 判断是否应该删除
            if (userToReasonMap.size() > 10) {
                String firstKey = MapUtil.getFirstOrNull(userToReasonMap);
                userToReasonMap.remove(firstKey);
            }
        }
        metisException.setUserToReasonJson(JSON.toJSONString(userToReasonMap));
        metisException.updateById();
    }
}