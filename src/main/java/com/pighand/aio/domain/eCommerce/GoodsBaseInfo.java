package com.pighand.aio.domain.ECommerce;

import com.pighand.framework.spring.base.BaseDomainRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;

/**
 * 电商 - 库存抽象
 *
 * @author wangshuli
 * @createDate 2024-01-07 19:55:48
 */
@Data
public class GoodsBaseInfo extends BaseDomainRecord<GoodsBaseInfo> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long spuId;

    @Schema(description = "门店id")
    private Long storeId;

    @Schema(description = "库存")
    private Integer stock;

    @Schema(description = "原价")
    private Long originalPrice;

    @Schema(description = "现价")
    private Long currentPrice;
}
