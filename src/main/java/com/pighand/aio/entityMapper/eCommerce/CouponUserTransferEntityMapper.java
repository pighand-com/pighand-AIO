package com.pighand.aio.entityMapper.eCommerce;

import com.pighand.aio.domain.eCommerce.CouponUserTransferDomain;
import com.pighand.aio.vo.eCommerce.CouponUserTransferVO;
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
