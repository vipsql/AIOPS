package com.coocaa.detector.util.metis;

/**
 * @author 陈煜坤
 * @date 2019/8/7  17:14
 * @package_name com.monitoring.warn.notice.utils.metis
 */
public class SourceUtils {

    private static final String MODEL_SUFFIX_NAME = "_model";

    public static String modifyModelSuffixName(String string){
        if (string.contains(MODEL_SUFFIX_NAME)){
            string = string.replaceFirst(MODEL_SUFFIX_NAME,"");
            if (string.contains(MODEL_SUFFIX_NAME)){
                return "";
            }
            return string;
        }else {
            return "";
        }
    }
}
