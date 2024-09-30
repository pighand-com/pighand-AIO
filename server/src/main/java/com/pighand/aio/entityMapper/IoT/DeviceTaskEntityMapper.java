package com.pighand.aio.entityMapper.IoT;

import com.pighand.aio.domain.IoT.DeviceTaskDomain;
import com.pighand.aio.vo.IoT.DeviceTaskVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * IoT - 设备任务
 *
 * @author wangshuli
 * @createDate 2024-04-10 23:45:23
 */
@Mapper(componentModel = "spring")
public interface DeviceTaskEntityMapper {

    DeviceTaskVO toVo(DeviceTaskDomain entity);

    DeviceTaskDomain toDomain(DeviceTaskVO vo);

    List<DeviceTaskVO> toVoList(List<DeviceTaskDomain> entity);

    List<DeviceTaskDomain> toDomainList(List<DeviceTaskVO> vo);
}
