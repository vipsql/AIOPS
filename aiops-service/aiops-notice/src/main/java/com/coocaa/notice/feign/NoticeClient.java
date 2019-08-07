package com.coocaa.notice.feign;

import com.coocaa.core.tool.api.R;
import com.coocaa.notice.entity.Mail;
import com.coocaa.notice.service.IMailService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * @program: intelligent-maintenance
 * @description: Feign实现
 * @author: dongyang_wu
 * @create: 2019-08-07 11:45
 */
@RestController
@AllArgsConstructor
@ApiIgnore
public class NoticeClient implements INoticeClient {
    private IMailService mailService;

    @Override
    public R<Boolean> sendMail(Mail mail) {
        return R.data(mailService.addMailNoticeToMQ(mail));
    }

    @Override
    public R<Boolean> sendMails(List<Mail> mails) {
        mails.forEach(mail -> mailService.addMailNoticeToMQ(mail));
        return R.success();
    }
}