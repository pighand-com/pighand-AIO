package com.pighand.aio.entityMapper.ECommerce;

import com.pighand.aio.domain.ECommerce.TicketUserValidityDomain;
import com.pighand.aio.vo.ECommerce.TicketUserValidityVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 电商 - 票务 - 已购票使用范围
 *
 * @author wangshuli
 * @createDate 2024-04-28 11:36:03
 */
@Mapper(componentModel = "spring")
public interface TicketUserValidityEntityMapper {

    TicketUserValidityVO toVo(TicketUserValidityDomain entity);

    TicketUserValidityDomain toDomain(TicketUserValidityVO vo);

    List<TicketUserValidityVO> toVoList(List<TicketUserValidityDomain> entity);

    List<TicketUserValidityDomain> toDomainList(List<TicketUserValidityVO> vo);
}
