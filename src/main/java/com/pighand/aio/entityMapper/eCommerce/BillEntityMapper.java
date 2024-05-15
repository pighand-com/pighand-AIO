package com.pighand.aio.entityMapper.ECommerce;

import com.pighand.aio.domain.ECommerce.BillDomain;
import com.pighand.aio.vo.ECommerce.BillVO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * 商城 - 账单
 *
 * @author wangshuli
 * @createDate 2024-04-18 14:35:34
 */
@Mapper(componentModel = "spring")
public interface BillEntityMapper {

    BillVO toVo(BillDomain entity);

    BillDomain toDomain(BillVO vo);

    List<BillVO> toVoList(List<BillDomain> entity);

    List<BillDomain> toDomainList(List<BillVO> vo);
}
