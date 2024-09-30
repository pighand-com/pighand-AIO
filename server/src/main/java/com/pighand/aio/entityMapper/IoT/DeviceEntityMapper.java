package com.pighand.aio.entityMapper.IoT;

import com.pighand.aio.domain.IoT.DeviceDomain;
import com.pighand.aio.vo.IoT.DeviceVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * IoT - 设备
 *
 * @author wangshuli
 * @createDate 2024-04-10 23:45:23
 */
@Mapper(componentModel = "spring")
public interface DeviceEntityMapper {

    DeviceVO toVo(DeviceDomain entity);

    DeviceDomain toDomain(DeviceVO vo);

    List<DeviceVO> toVoList(List<DeviceDomain> entity);

    List<DeviceDomain> toDomainList(List<DeviceVO> vo);
}
