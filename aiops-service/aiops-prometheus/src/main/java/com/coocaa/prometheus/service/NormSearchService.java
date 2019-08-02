package com.coocaa.prometheus.service;


import com.coocaa.prometheus.entity.*;

import java.util.Date;
import java.util.List;

/**
 * @Auther: wyx
 * @Date: 2019-07-28 16:47
 * @Description: 指标查询服务
 */
public interface NormSearchService {

    /**
     * 查询指定时间点的请求数
     * @param date 时间点
     * @param timeout 超时时间
     * @return
     */
    List<VectorData> queryInstantRequests(Date date, Integer timeout);

    /**
     * 查询指定时间范围内的请求数
     * @param start 开始时间
     * @param end 结束时间
     * @param step 查询时间步长
     * @return
     */
    List<MatrixData> queryRangeRequests(Date start, Date end, Integer step);

    /**
     * 查询指定时间点的 CPU 负载
     * @param date
     * @param timeout
     * @return
     */
    List<VectorData> queryInstantCPULoad(Date date, Integer timeout);

    /**
     * 查询指定时间范围内的 CPU 负载
     * @param start
     * @param end
     * @param step
     * @return
     */
    List<MatrixData> queryRangeCPULoad(Date start, Date end, Integer step);

    /**
     * 查询指定时间点的磁盘容量
     * @param date
     * @param timeout
     * @return
     */
    List<VectorData> queryInstantDisk(Date date, Integer timeout);

    /**
     * 查询指定时间范围内的磁盘容量
     * @param start
     * @param end
     * @param step
     * @return
     */
    List<MatrixData> queryRangeDisk(Date start, Date end, Integer step);

    /**
     * 查询指定时间点的上行带宽
     * @param date
     * @param timeout
     * @return
     */
    List<VectorData> queryInstantUpLoadBandWidth(Date date, Integer timeout);

    /**
     * 查询指定时间范围内的上行带宽
     * @param start
     * @param end
     * @param step
     * @return
     */
    List<MatrixData> queryRangeUpLoadBandWidth(Date start, Date end, Integer step);

    /**
     * 查询指定时间点的下行带宽
     * @param date
     * @param timeout
     * @return
     */
    List<VectorData> queryInstantDownLoadBandWidth(Date date, Integer timeout);

    /**
     * 查询指定时间范围内的下行带宽
     * @param start
     * @param end
     * @param step
     * @return
     */
    List<MatrixData> queryRangeDownLoadBandWidth(Date start, Date end, Integer step);

    /**
     * 查询指定时间范围内的全部数据
     * @param start
     * @param end
     * @param step
     * @return
     */
    PromMatrixData queryRangeData(Date start, Date end, Integer step);

}
