package com.pighand.aio.vo.eCommerce;

import com.pighand.aio.domain.eCommerce.GoodsSpuDomain;
import lombok.Data;

import java.util.List;

/**
 * 电商 - SPU
 *
 * @author wangshuli
 * @createDate 2024-01-08 11:05:18
 */
@Data
public class GoodsSpuVO extends GoodsSpuDomain {

    // relation table: begin
    private GoodsCategoryVO goodsCategory;

    private List<GoodsSkuVO> goodsSku;
    // relation table: end
}
