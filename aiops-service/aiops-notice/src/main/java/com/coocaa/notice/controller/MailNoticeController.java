package com.coocaa.notice.controller;

import com.coocaa.core.log.response.ResponseHelper;
import com.coocaa.core.log.response.ResultBean;
import com.coocaa.notice.entity.Mail;
import com.coocaa.notice.service.IMailService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;


/**
 * @date 2019/7/23  9:24
 * @package_name com.monitoring.warn_notice
 */
@RestController
@AllArgsConstructor
@Api(value = "通知模块", tags = "通知接口")
public class MailNoticeController {

    private IMailService mailService;

    @PostMapping("/send")
    @ApiOperation("邮件通知")
    public ResponseEntity<ResultBean> sendMail(@RequestBody @Validated Mail mail, @ApiIgnore Errors errors) {
        if (errors.hasErrors()) {
            String msg = errors.getFieldError().getDefaultMessage();
            return ResponseHelper.BadRequest(msg);
        }
        return ResponseHelper.OK(mailService.addMailNoticeToMQ(mail));
    }

}
