package com.pighand.aio.vo.ECommerce;

import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.ECommerce.CouponUserTransferDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 电商 - 优惠券 - 转赠记录
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Data
@TableRef(CouponUserTransferDomain.class)
@EqualsAndHashCode(callSuper = false)
public class CouponUserTransferVO extends CouponUserTransferDomain {
}
