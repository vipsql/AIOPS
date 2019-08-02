package com.coocaa.user.service.impl;

import com.coocaa.core.mybatis.base.BaseServiceImpl;
import com.coocaa.user.entity.Machine;
import com.coocaa.user.mapper.MachineMapper;
import com.coocaa.user.service.MachineService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @program: intelligent_maintenance
 * @description:
 * @author: dongyang_wu
 * @create: 2019-07-31 10:58
 */
@Service("MachineService")
@AllArgsConstructor
public class MachineServiceImpl extends BaseServiceImpl<MachineMapper, Machine> implements MachineService {
}