package com.coocaa.detector.entity;

/**
 * @program: intelligent_maintenance
 * @description: 检测返回值
 * @author: dongyang_wu
 * @create: 2019-08-02 11:48
 */
public class DetectorResult {

    /**
     * code : 0
     * msg : 操作成功
     * data : {"ret":0,"p":"0.0021384065"}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * ret : 0
         * p : 0.0021384065
         */

        private int ret;
        private String p;

        public int getRet() {
            return ret;
        }

        public void setRet(int ret) {
            this.ret = ret;
        }

        public String getP() {
            return p;
        }

        public void setP(String p) {
            this.p = p;
        }
    }
}