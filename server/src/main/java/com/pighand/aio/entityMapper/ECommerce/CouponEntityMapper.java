package com.pighand.aio.entityMapper.ECommerce;

import com.pighand.aio.domain.ECommerce.CouponDomain;
import com.pighand.aio.vo.ECommerce.CouponVO;
import org.mapstruct.Mapper;

/**
 * 电商 - 优惠券
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Mapper(componentModel = "spring")
public interface CouponEntityMapper {

    CouponVO toVo(CouponDomain entity);

    CouponDomain toDomain(CouponVO vo);
}
