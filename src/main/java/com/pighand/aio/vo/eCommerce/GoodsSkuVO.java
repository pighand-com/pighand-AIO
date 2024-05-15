package com.pighand.aio.vo.ECommerce;

import com.pighand.aio.domain.ECommerce.GoodsSkuDomain;
import lombok.Data;

/**
 * 电商 - SKU
 *
 * @author wangshuli
 * @createDate 2024-01-08 11:05:18
 */
@Data
public class GoodsSkuVO extends GoodsSkuDomain {
    private GoodsSpuVO goodsSpu;

    // 购买数量
    private Integer quantity;
}
