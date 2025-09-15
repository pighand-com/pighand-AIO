package com.pighand.aio.entityMapper.MKT;

import com.pighand.aio.domain.MKT.CheckInLocationDomain;
import com.pighand.aio.vo.MKT.CheckInLocationVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 营销 - 打卡地点
 *
 * @author wangshuli
 * @createDate 2024-12-30
 */
@Mapper(componentModel = "spring")
public interface CheckInLocationEntityMapper {

    CheckInLocationVO toVo(CheckInLocationDomain entity);

    CheckInLocationDomain toDomain(CheckInLocationVO vo);

    List<CheckInLocationVO> toVoList(List<CheckInLocationDomain> entity);

    List<CheckInLocationDomain> toDomainList(List<CheckInLocationVO> vo);
}