package com.pighand.aio.vo.ECommerce;

import com.pighand.aio.domain.ECommerce.CouponDomain;
import com.pighand.aio.domain.ECommerce.CouponUserDomain;
import com.pighand.aio.vo.user.UserVO;
import lombok.Data;

/**
 * 电商 - 优惠券 - 用户已领
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Data
public class CouponUserVO extends CouponUserDomain {
    private CouponDomain coupon;

    private UserVO owner;

    private String queryPhone;
}
