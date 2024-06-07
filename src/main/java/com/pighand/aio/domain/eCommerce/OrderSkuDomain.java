package com.pighand.aio.domain.ECommerce;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.pighand.aio.common.enums.OrderSKUTypeEnum;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import com.pighand.framework.spring.base.BaseDomainRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 电商 - 订单 - SKU
 *
 * @author wangshuli
 * @createDate 2024-04-18 14:35:34
 */
@Table("ec_order_sku")
@Data
public class OrderSkuDomain extends BaseDomainRecord<OrderSkuDomain> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("orderSkuCreate")
    @RequestFieldException("orderSkuUpdate")
    private Long id;
    @Schema(description = "订单ID")
    private Long orderId;
    @Length(max = 32)
    @Schema(description = "交易单ID")
    private Long orderTradeId;
    @Schema(description = "SPU ID")
    private Long spuId;
    @Schema(description = "SKU ID")
    private Long skuId;
    @Schema(description = "票务ID")
    private Long ticketId;
    @Schema(description = "场次ID")
    private Long sessionId;
    @Column("type")
    @Schema(description = "类型 10-SPU 11-SKU 20-票务 30-场次")
    private OrderSKUTypeEnum type;
    @Schema(description = "购买数量")
    private Integer quantity;
    @Schema(description = "应付金额（分）")
    private Long amountPayable;
    @Schema(description = "实付金额（分）")
    private Long amountPaid;
}
