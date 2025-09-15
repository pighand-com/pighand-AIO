package com.pighand.aio.entityMapper.MKT;

import com.pighand.aio.domain.MKT.CheckInUserDomain;
import com.pighand.aio.vo.MKT.CheckInUserVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 营销 - 打卡用户参与信息
 *
 * @author wangshuli
 * @createDate 2024-12-30
 */
@Mapper(componentModel = "spring")
public interface CheckInUserEntityMapper {

    CheckInUserVO toVo(CheckInUserDomain entity);

    CheckInUserDomain toDomain(CheckInUserVO vo);

    List<CheckInUserVO> toVoList(List<CheckInUserDomain> entity);

    List<CheckInUserDomain> toDomainList(List<CheckInUserVO> vo);
}