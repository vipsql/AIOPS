package com.coocaa.notice.feign;

import com.coocaa.core.secure.constant.AppConstant;
import com.coocaa.core.tool.api.R;
import com.coocaa.notice.entity.Mail;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @description: 通知Feign
 * @author: dongyang_wu
 * @create: 2019-08-07 11:41
 */
@FeignClient(
        value = AppConstant.APPLICATION_NOTICE_NAME,
        fallback = INoticeClientFallback.class
)
public interface INoticeClient {
    String API_PREFIX = "/notice";

    @PostMapping(API_PREFIX + "/sendMail")
    R<Boolean> sendMail(@RequestBody Mail mail);

    @PostMapping(API_PREFIX + "/sendMails")
    R<Boolean> sendMails(@RequestBody List<Mail> mails);
}