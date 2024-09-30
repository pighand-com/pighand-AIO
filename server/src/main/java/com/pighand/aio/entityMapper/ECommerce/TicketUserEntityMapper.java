package com.pighand.aio.entityMapper.ECommerce;

import com.pighand.aio.domain.ECommerce.TicketUserDomain;
import com.pighand.aio.vo.ECommerce.TicketUserVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 电商 - 已购票务
 *
 * @author wangshuli
 * @createDate 2024-04-26 14:52:18
 */
@Mapper(componentModel = "spring")
public interface TicketUserEntityMapper {

    TicketUserVO toVo(TicketUserDomain entity);

    TicketUserDomain toDomain(TicketUserVO vo);

    List<TicketUserVO> toVoList(List<TicketUserDomain> entity);

    List<TicketUserDomain> toDomainList(List<TicketUserVO> vo);
}
