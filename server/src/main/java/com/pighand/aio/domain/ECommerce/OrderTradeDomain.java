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
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;

/**
 * 电商 - 订单 - 交易单
 *
 * @author wangshuli
 * @createDate 2024-04-18 14:35:34
 */
@Table("ec_order_trade")
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderTradeDomain extends BaseDomainRecord<OrderTradeDomain> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("orderTradeCreate")
    @RequestFieldException("orderTradeUpdate")
    private Long id;

    @Length(max = 32)
    @Schema(description = "流水号")
    private String sn;

    @Schema(description = "应付金额（分）")
    private Long amountPayable;

    @Schema(description = "实付金额（分）")
    private Long amountPaid;

    @Length(max = 32)
    @Schema(description = "创建人")
    private Long creatorId;

    @Schema(description = "创建时间")
    private Date createdAt;
}
