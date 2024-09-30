package com.pighand.aio.vo.ECommerce;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.pighand.aio.domain.ECommerce.GoodsSpuDomain;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
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
    private List<OrderSkuVO> orderSku;
    // relation table: end

    // 所属门店id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long storeId;
    // 购买数量
    private Integer quantity;

    private String system;
}
