package com.pighand.aio.vo.ECommerce;

import com.pighand.aio.domain.ECommerce.CouponDomain;
import com.pighand.aio.domain.ECommerce.CouponUserDomain;
import com.pighand.aio.vo.base.UserVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 电商 - 优惠券 - 用户已领
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CouponUserVO extends CouponUserDomain {
    private List<CouponUserTransferVO> couponUserTransfer;

    private CouponDomain coupon;

    private UserVO owner;

    private String queryPhone;
}
