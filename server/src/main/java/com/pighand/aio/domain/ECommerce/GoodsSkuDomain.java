package com.pighand.aio.domain.ECommerce;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 电商 - SKU
 *
 * @author wangshuli
 * @createDate 2024-01-07 19:55:48
 */
@Table("ec_goods_sku")
@Data
public class GoodsSkuDomain extends GoodsBaseInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("goodsSkuCreate")
    @RequestFieldException("goodsSkuUpdate")
    private Long id;

    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long applicationId;

    @Length(max = 32)
    @Schema(description = "属性。格式：attributeKey1_attributeVlaue1,attributeKey2_attributeVlaue2")
    private String attributes;

    @Column("name")
    @Length(max = 64)
    @Schema(description = "Sku名称。一般是规格明文组合")
    private String name;

    @Schema(description = "可用抵扣代币")
    private Long offsetToken;

    @Schema(description = "赠送代币")
    private Long giftToken;

    private Boolean deleted;
}
