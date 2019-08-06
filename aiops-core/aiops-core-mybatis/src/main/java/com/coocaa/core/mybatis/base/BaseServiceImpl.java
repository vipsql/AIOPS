/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.coocaa.core.mybatis.base;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.coocaa.common.request.RequestBean;
import com.coocaa.core.secure.AiOpsUser;
import com.coocaa.core.secure.utils.SecureUtil;
import com.coocaa.core.tool.utils.BeanUtil;
import com.coocaa.core.tool.utils.SqlUtil;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotEmpty;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 业务封装基础类
 *
 * @param <M> mapper
 * @param <T> model
 * @author Chill
 */
@Validated
public class BaseServiceImpl<M extends BaseMapper<T>, T extends Model> extends ServiceImpl<M, T> implements BaseService<T> {

    private Class<T> modelClass;

    @SuppressWarnings("unchecked")
    public BaseServiceImpl() {
        Type type = this.getClass().getGenericSuperclass();
        this.modelClass = (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[1];
    }

    @Override
    public boolean save(T entity) {
        AiOpsUser user = SecureUtil.getUser();
        LocalDateTime now = LocalDateTime.now();
//		entity.setCreateUser(user != null ? Objects.requireNonNull(user).getUserId() : null);
//		entity.setCreateTime(now);
//		entity.setUpdateUser(user != null ? user.getUserId() : null);
//		entity.setUpdateTime(now);
//		entity.setStatus(AiOpsConstant.DB_STATUS_NORMAL);
//		entity.setIsDeleted(AiOpsConstant.DB_NOT_DELETED);
        return super.save(entity);
    }

    @Override
    public boolean updateById(T entity) {
        AiOpsUser user = SecureUtil.getUser();
//		entity.setUpdateUser(Objects.requireNonNull(user).getUserId());
//		entity.setUpdateTime(LocalDateTime.now());
        return super.updateById(entity);
    }

    @Override
    public boolean deleteLogic(@NotEmpty List<Integer> ids) {
        AiOpsUser user = SecureUtil.getUser();
        T entity = BeanUtil.newInstance(modelClass);
//		entity.setUpdateUser(Objects.requireNonNull(user).getUserId());
//		entity.setUpdateTime(LocalDateTime.now());
        return super.removeByIds(ids);
    }

    public void deletes(RequestBean requestBean) {
        requestBean.getItems().forEach(item -> {
            List<T> ts = super.baseMapper.selectByMap(SqlUtil.map(item.getQuery(), item.getQueryString()).build());
            ts.forEach(Model::deleteById);
        });
    }
}
