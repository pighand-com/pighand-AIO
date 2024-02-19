package com.pighand.aio.vo.eCommerce;

import com.pighand.aio.domain.eCommerce.CouponDomain;
import com.pighand.aio.domain.eCommerce.CouponUserDomain;
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
