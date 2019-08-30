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
            " and ( ${conditions} ) " +
            "</if>" +
            "ORDER BY ${orderBy} ${sortType} " +
            "limit #{page},#{count}" +
            "</script>";

    String GET_PAGE_ALL_SIZE = "<script>" + TableConstant.SELECT_COUNT;
    String GET_PAGE_ALL_SIZE_CONDITION = TableConstant.LOGIC_EXIST_STR +
            "<if test='conditions!=null '>" +
            " and ( ${conditions} ) " +
            "</if></script>";

    String GET_ALL_CONDITION = TableConstant.LOGIC_EXIST_STR +
            "<if test='conditions!=null '>" +
            " and ( ${conditions} ) " +
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
    String SELECT_ID_ALL = "SELECT id FROM ";
    String WHERE = " WHERE ";
    /**
     * 时间常量
     */
    String CREATE_TIME = "create_time";
    /**
     * Team 与或非常量
     */
    String TEAM_CONDITON = " FIND_IN_SET(%s,team_ids) ";

    interface TABLE {
        String TABLE_TASK = "task";
        String TABLE_USER = "user";
        String TABLE_TEAM = "team";
        String TABLE_METRICS = "metrics";
        String TABLE_METIS_EXCEPTION = "metis_exception";
        String TABLE_KPI = "kpi";
        String LOG_API = "log_api";
    }

    interface USER {
        String MAIL = "mail";
        String TEAM_IDS = "team_ids";
        String ADMIN_USER_ID = "admin_user_id";
    }

    interface TASK {
        Integer START_PAGE = 0;
        Integer START_COUNT = 5;
        Integer START_ERROR_FLAG_NUMBER = 100;
        String TASK_ID = "task_id";
        String TASK_NAME = "task_name";
        String METRICS_ID = "metrics_id";
        String IS_UP = "is_up";
        String ERROR_NUMBER = "error_number";
        String INSTANCE = "instance";
    }

    interface TEAM {
        String NAME = "name";
    }
}