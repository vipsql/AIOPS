package com.coocaa.common.constant;

/**
 * @description: 数据表常量
 * @author: dongyang_wu
 * @create: 2019-07-31 09:20
 */
public interface TableConstant {
    /**
     * 用于分页显示基本脚本常量开始
     */
    String GET_PAGE_ALL = "<script>" + TableConstant.SELECT_ALL;
    String GET_PAGE_ALL_CONDITION = TableConstant.LOGIC_EXIST_STR +
            "<if test='conditions!=null '>" +
            "and ${conditions} " +
            "</if>" +
            "ORDER BY ${orderBy} ${sortType} " +
            "limit #{page},#{count}" +
            "</script>";

    String GET_PAGE_ALL_SIZE = "<script>" + TableConstant.SELECT_COUNT;
    String GET_PAGE_ALL_SIZE_CONDITION = TableConstant.LOGIC_EXIST_STR +
            "<if test='conditions!=null '>" +
            "and ${conditions} " +
            "</if></script>";
    /**
     * 用于分页显示基本脚本常量结束
     */
    String LOGIC_EXIST_STR = TableConstant.WHERE + TableConstant.LOGIC_EXIST;
    String ID = "id";
    String STATUS = "status";
    String LOGIC = "logic";
    Integer LOGIC_EXIST_CONSTANT = 0;
    Integer LOGIC_NOT_EXIST_CONSTANT = 1;
    String LOGIC_EXIST = TableConstant.LOGIC + " = 0 ";
    String SELECT_COUNT = "SELECT COUNT(*) FROM ";
    String SELECT_ALL = "SELECT * FROM ";
    String WHERE = " WHERE ";

    interface TABLE {
        String TABLE_TASK = "task";
        String TABLE_USER = "user";
        String TABLE_TEAM = "team";
        String TABLE_METRICS = "metrics";
        String TABLE_METIS_EXCEPTION = "metis_exception";
    }

    interface USER {
        String MAIL = "mail";
        String TEAM_IDS = "team_ids";
    }

    interface TASK {
        String TASK_ID = "task_id";
        String METRICS_ID = "metrics_id";
    }

    interface TEAM {
    }
}