package com.pighand.aio.entityMapper.ECommerce;

import com.pighand.aio.domain.ECommerce.CouponUserTransferDomain;
import com.pighand.aio.vo.ECommerce.CouponUserTransferVO;
import org.mapstruct.Mapper;

/**
 * 电商 - 优惠券 - 转赠记录
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Mapper(componentModel = "spring")
public interface CouponUserTransferEntityMapper {

    CouponUserTransferVO toVo(CouponUserTransferDomain entity);

    CouponUserTransferDomain toDomain(CouponUserTransferVO vo);
}
