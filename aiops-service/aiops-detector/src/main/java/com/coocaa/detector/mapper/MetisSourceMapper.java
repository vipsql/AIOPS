package com.coocaa.detector.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.coocaa.detector.entity.Model;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * @author 陈煜坤
 * @date 2019/8/7  16:43
 * @package_name com.monitoring.warn.notice.mapper
 */
@Mapper
public interface MetisSourceMapper {

    @Select("select source from sample_dataset GROUP BY source")
    String[] getAllQueryTrainSource();

    @Select("select model_name from train_task")
    String[] getAllModelSource();

    @Select("select model_name from train_task where source LIKE CONCAT(CONCAT('%', #{0}), '%')")
    String[] getModelSource(String sourceName);

    @Select("select status from train_task where model_name=CONCAT(#{0},'_model')")
    String getModelStatus(String str);

    @Select("select sample_num,start_time,end_time from train_task")
    List<Model> getAllModelTime();

    @Select("select * from train_task")
    List<Model> getAllModel(Page page);

}
