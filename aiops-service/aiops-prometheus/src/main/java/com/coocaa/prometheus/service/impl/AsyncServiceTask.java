package com.coocaa.prometheus.service.impl;

import com.coocaa.common.constant.StringConstant;
import com.coocaa.common.util.RegexUtil;
import com.coocaa.core.tool.api.R;
import com.coocaa.notice.entity.Mail;
import com.coocaa.notice.feign.INoticeClient;
import com.coocaa.prometheus.entity.MatrixData;
import com.coocaa.prometheus.mapper.KpiMapper;
import com.coocaa.user.entity.User;
import com.coocaa.user.feign.IUserClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.*;

/**
 * @description: 异步任务
 * @author: dongyang_wu
 * @create: 2019-08-07 11:57
 */
@Component
@Slf4j
@AllArgsConstructor
public class AsyncServiceTask {
    private INoticeClient noticeClient;
    private KpiMapper kpiMapper;
    private IUserClient userClient;

    @Async
    public ListenableFuture<String> sendDetectResult(Map<String, MatrixData> matrixDatas, String teamIds, String taskName) {
        log.info("开始发送邮件给小组成员");
        // 判断数据是否异常，异常则加入异常列表并发通知给负责人
        // 发给team中的所有成员
        R<Set<User>> rpcResult = userClient.getTeamUsers(teamIds, StringConstant.OR);
        if (rpcResult.isSuccess()) {
            Set<User> users = rpcResult.getData();
            System.out.println(users);
            // 调用发送邮件接口发送具体消息
            String content = getMailContent(matrixDatas);
            List<Mail> mailList = new ArrayList<>();
            users.forEach(user -> {
                if (StringUtils.isEmpty(user.getMail()) || !RegexUtil.checkEmail(user.getMail()))
                    return;
                mailList.add(getMail(user.getMail(), taskName + "指标异常通知", content));
            });
            noticeClient.sendMails(mailList);
        }
        return new AsyncResult<>(StringConstant.OK);
    }

    //    @Async
    public Mail getMail(String to, String title, String content) {
        Mail mail = Mail.builder()
                .to(to)
                .title(title)
                .content(content).build();
        return mail;
//        noticeClient.sendMail(mail);
    }

    private String getMailContent(Map<String, MatrixData> matrixDatas) {
        StringBuffer content = new StringBuffer();
        matrixDatas.forEach((key, value) -> content.append(value.getMetric()).append("经Metis检测异常，概率为：").append(value.getDetectResult().getP()).append("<br>"));
        return content.toString();
    }
}