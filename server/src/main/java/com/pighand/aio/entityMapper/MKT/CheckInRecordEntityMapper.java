package com.pighand.aio.entityMapper.MKT;

import com.pighand.aio.domain.MKT.CheckInRecordDomain;
import com.pighand.aio.vo.MKT.CheckInRecordVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 营销 - 打卡签到记录
 *
 * @author wangshuli
 * @createDate 2024-12-30
 */
@Mapper(componentModel = "spring")
public interface CheckInRecordEntityMapper {

    CheckInRecordVO toVo(CheckInRecordDomain entity);

    CheckInRecordDomain toDomain(CheckInRecordVO vo);

    List<CheckInRecordVO> toVoList(List<CheckInRecordDomain> entity);

    List<CheckInRecordDomain> toDomainList(List<CheckInRecordVO> vo);
}