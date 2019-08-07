package com.coocaa.core.mybatis.base;

import com.baomidou.mybatisplus.extension.service.IService;
import com.coocaa.common.request.RequestBean;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 基础业务接口
 *
 * @param <T>
 * @author dongyang_wu
 */
public interface BaseService<T> extends IService<T> {

    /**
     * 逻辑删除
     *
     * @param ids id集合(逗号分隔)
     * @return
     */
    boolean deleteLogic(@NotEmpty List<Integer> ids);

    /**
     * 批量删除
     *
     * @param requestBean
     */
    void deletes(RequestBean requestBean);
}
