package com.coocaa.notice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.coocaa.core.tool.jackson.JsonUtil;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MsgLog extends Model<MsgLog> {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String msg;
    private String exchange;
    private String routingKey;
    private Integer status;
    private Integer tryCount;
    private Date nextTryTime;
    private Date createTime;
    private Date updateTime;

    public void setMsg(Object msg) {
        this.msg = JsonUtil.toJson(msg);
    }
}
