package com.pighand.aio.vo.ECommerce;

import com.pighand.aio.domain.ECommerce.CouponDomain;
import com.pighand.aio.vo.base.StoreVO;
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

    private List<CouponUserVO> couponUser;

    @Schema(description = "持有数量")
    private Integer holdCount;
}
