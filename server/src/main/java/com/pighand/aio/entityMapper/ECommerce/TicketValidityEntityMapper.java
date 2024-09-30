package com.pighand.aio.entityMapper.ECommerce;

import com.pighand.aio.domain.ECommerce.TicketValidityDomain;
import com.pighand.aio.vo.ECommerce.TicketValidityVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 电商 - 票务 - 使用范围
 *
 * @author wangshuli
 * @createDate 2024-04-28 11:36:03
 */
@Mapper(componentModel = "spring")
public interface TicketValidityEntityMapper {

    TicketValidityVO toVo(TicketValidityDomain entity);

    TicketValidityDomain toDomain(TicketValidityVO vo);

    List<TicketValidityVO> toVoList(List<TicketValidityDomain> entity);

    List<TicketValidityDomain> toDomainList(List<TicketValidityVO> vo);
}
