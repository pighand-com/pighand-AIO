package com.pighand.aio.domain.ECommerce;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.handler.JacksonTypeHandler;
import com.pighand.aio.common.enums.GoodsSpuStatusEnum;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import com.pighand.framework.spring.api.springdoc.dataType.EmptyObject;
import com.pighand.framework.spring.base.BaseDomainRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.List;

/**
 * 电商 - SPU
 *
 * @author wangshuli
 * @createDate 2024-01-07 19:55:48
 */
@Table("ec_goods_spu")
@Data
public class GoodsSpuDomain extends BaseDomainRecord<GoodsSpuDomain> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("goodsSpuCreate")
    @RequestFieldException("goodsSpuUpdate")
    private Long id;

    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long projectId;

    @Schema(description = "商户id")
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long businessId;

    @Schema(description = "门店id")
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long storeId;

    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long goodsCategoryId;

    @Column("name")
    @Length(max = 16)
    @Schema(description = "名称")
    private String name;

    @Schema(description = "图片", implementation = EmptyObject.class)
    @Column(typeHandler = JacksonTypeHandler.class)
    private List<String> imageUrls;

    @Schema(description = "最低现价sku对应的原价（分）")
    private Long originalPrice;

    @Schema(description = "最低现价（分）")
    private Long currentPrice;

    @Schema(description = "详情")
    private String details;

    @Schema(description = "状态 10上架 20下架")
    private GoodsSpuStatusEnum status;

    private Boolean deleted;

}
