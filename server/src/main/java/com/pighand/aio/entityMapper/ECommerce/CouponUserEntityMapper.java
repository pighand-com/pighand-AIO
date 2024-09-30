package com.pighand.aio.entityMapper.ECommerce;

import com.pighand.aio.domain.ECommerce.CouponUserDomain;
import com.pighand.aio.vo.ECommerce.CouponUserVO;
import org.mapstruct.Mapper;

/**
 * 电商 - 优惠券 - 用户已领
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Mapper(componentModel = "spring")
public interface CouponUserEntityMapper {

    CouponUserVO toVo(CouponUserDomain entity);

    CouponUserDomain toDomain(CouponUserVO vo);
}
