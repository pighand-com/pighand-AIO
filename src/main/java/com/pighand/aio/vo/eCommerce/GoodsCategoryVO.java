package com.pighand.aio.vo.eCommerce;

import com.pighand.aio.domain.eCommerce.GoodsCategoryDomain;
import lombok.Data;

import java.util.List;

/**
 * 电商 - 商品类目
 *
 * @author wangshuli
 * @createDate 2024-01-08 11:05:18
 */
@Data
public class GoodsCategoryVO extends GoodsCategoryDomain {
    private List<GoodsSpuVO> goodsSpu;
}
