package com.coocaa.common.request;

import lombok.*;

import java.util.List;

/**
 * @description: 请求分页参数
 * @author: dongyang_wu
 * @create: 2019-08-06 09:58
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageWithTeamRequestBean extends PageRequestBean {
    private List<PageRequestBean.PageTeamRequestItem> teamConditions;
}