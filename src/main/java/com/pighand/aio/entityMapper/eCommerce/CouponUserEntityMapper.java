package com.pighand.aio.entityMapper.eCommerce;

import com.pighand.aio.domain.eCommerce.CouponUserDomain;
import com.pighand.aio.vo.eCommerce.CouponUserVO;
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
