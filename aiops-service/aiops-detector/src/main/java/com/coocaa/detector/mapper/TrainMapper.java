package com.coocaa.detector.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.coocaa.detector.entity.SampleDataset;
import com.coocaa.detector.entity.Train;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * @author 陈煜坤
 * @date 2019/8/19  9:15
 * @package_name com.monitoring.warn.notice.mapper
 */
public interface TrainMapper extends BaseMapper<Train> {

    @Select("select * from sample_dataset")
    List<SampleDataset> getSD(QueryWrapper qw);
}
