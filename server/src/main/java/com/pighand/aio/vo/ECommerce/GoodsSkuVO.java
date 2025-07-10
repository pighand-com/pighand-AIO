package com.pighand.aio.vo.ECommerce;

import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.ECommerce.GoodsSkuDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 电商 - SKU
 *
 * @author wangshuli
 * @createDate 2024-01-08 11:05:18
 */
@Data
@TableRef(GoodsSkuDomain.class)
@EqualsAndHashCode(callSuper = false)
public class GoodsSkuVO extends GoodsSkuDomain {
    private List<OrderSkuVO> orderSku;

    private GoodsSpuVO goodsSpu;

    // 购买数量
    private Integer quantity;
}
