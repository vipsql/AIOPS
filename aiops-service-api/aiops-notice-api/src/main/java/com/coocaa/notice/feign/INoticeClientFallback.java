package com.coocaa.notice.feign;

import com.coocaa.core.tool.api.R;
import com.coocaa.notice.entity.Mail;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @description: 通知Feign失败处理
 * @author: dongyang_wu
 * @create: 2019-08-07 11:43
 */
public class INoticeClientFallback implements INoticeClient {
    @Override
    public R<Boolean> sendMail(@RequestBody Mail mail) {
        return R.fail();
    }

    @Override
    public R<Boolean> sendMails(List<Mail> mails) {
        return R.fail();
    }
}