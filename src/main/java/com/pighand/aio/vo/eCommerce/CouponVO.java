package com.pighand.aio.vo.eCommerce;

import com.pighand.aio.domain.eCommerce.CouponDomain;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * 电商 - 优惠券
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Data
public class CouponVO extends CouponDomain {
    private StoreVO store;

    private List<CouponUserVO> couponUsers;

    @Schema(description = "持有数量")
    private Integer holdCount;
}
