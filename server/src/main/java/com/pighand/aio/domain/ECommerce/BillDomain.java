package com.pighand.aio.domain.ECommerce;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import com.pighand.framework.spring.base.BaseDomainRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;

/**
 * 商城 - 账单
 *
 * @author wangshuli
 * @createDate 2024-04-18 14:35:34
 */
@Table("ec_bill")
@Data
public class BillDomain extends BaseDomainRecord<BillDomain> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("billCreate")
    @RequestFieldException("billUpdate")
    private Long id;

    @Schema(description = "支付单ID")
    private Long orderTradeId;

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "订单sku ID")
    private Long orderSkuId;

    @Length(max = 32)
    @Schema(description = "退款单来源ID")
    private String sourceId;

    @Length(max = 32)
    @Schema(description = "支付单流水号")
    private String sn;

    @Schema(description = "账单类型 10支付 20退款")
    private Integer billType;

    @Schema(description = "外部平台类型 10微信")
    private Integer outTradePlatform;

    @Length(max = 32)
    @Schema(description = "外部平台支付流水号")
    private String outTradeNo;

    @Schema(description = "金额（分）")
    private Long amount;

    @Schema(description = "创建人")
    private Long creatorId;

    @Schema(description = "创建时间")
    private Date createdAt;
}
