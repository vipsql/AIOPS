package com.coocaa.core.tool.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.List;

/**
 * @Auther: wyx
 * @Date: 2019-07-25 15:27
 * @Description: BindingResult助手类
 */
@Slf4j
public class BindingResultUtil {

    public static void logError(BindingResult br){
        List<ObjectError> ls=br.getAllErrors();

        for (int i = 0; i < ls.size(); i++) {
            log.error(ls.get(i).getDefaultMessage());
        }
    }

}
