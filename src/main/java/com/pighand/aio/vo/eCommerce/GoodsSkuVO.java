package com.pighand.aio.vo.eCommerce;

import com.pighand.aio.domain.eCommerce.GoodsSkuDomain;
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
}
