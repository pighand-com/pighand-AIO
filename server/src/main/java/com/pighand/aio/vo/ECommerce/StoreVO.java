package com.pighand.aio.vo.ECommerce;

import com.mybatisflex.annotation.Column;
import com.pighand.aio.domain.ECommerce.StoreDomain;
import lombok.Data;

import java.util.List;

/**
 * 电商 - 门店
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Data
public class StoreVO extends StoreDomain {
    @Column(ignore = true)
    private List<CouponVO> coupons;
}
