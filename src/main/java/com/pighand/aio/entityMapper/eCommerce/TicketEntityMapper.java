package com.pighand.aio.entityMapper.ECommerce;

import com.pighand.aio.domain.ECommerce.TicketDomain;
import com.pighand.aio.vo.ECommerce.TicketVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 电商 - 票务
 *
 * @author wangshuli
 * @createDate 2024-04-26 14:52:18
 */
@Mapper(componentModel = "spring")
public interface TicketEntityMapper {

    TicketVO toVo(TicketDomain entity);

    TicketDomain toDomain(TicketVO vo);

    List<TicketVO> toVoList(List<TicketDomain> entity);

    List<TicketDomain> toDomainList(List<TicketVO> vo);
}
