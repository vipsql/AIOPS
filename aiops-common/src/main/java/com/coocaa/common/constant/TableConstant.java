package com.coocaa.common.constant;

/**
 * @program: intelligent_maintenance
 * @description: 数据表常量
 * @author: dongyang_wu
 * @create: 2019-07-31 09:20
 */
public interface TableConstant {
    String ID = "id";
    String STATUS = "status";
    String LOGIC = "logic";

    interface TABLE {
        String TABLE_TASK = "task";
        String TABLE_USER = "user";
        String TABLE_TEAM = "team";
    }

    interface USER {
        String MAIL = "mail";
        String TEAM_IDS = "team_ids";
    }

    interface TASK {
        String TASK_ID = "task_id";
    }

    interface TEAM {
        String ATTR = "SELECT id,name,admin_user_id,create_time,update_time FROM ";
    }
}