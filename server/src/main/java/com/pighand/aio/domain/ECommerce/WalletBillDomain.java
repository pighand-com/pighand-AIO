package com.pighand.aio.domain.ECommerce;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.pighand.aio.common.enums.WalletBillTypeEnum;
import com.pighand.aio.common.enums.WalletBillWalletTypeEnum;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import com.pighand.framework.spring.base.BaseDomainRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 电商 - 钱包账单
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Table("ec_wallet_bill")
@Data
@EqualsAndHashCode(callSuper = false)
public class WalletBillDomain extends BaseDomainRecord<WalletBillDomain> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("walletBillCreate")
    @RequestFieldException("walletBillUpdate")
    private Long id;

    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long applicationId;

    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @Schema(description = "账单类型 10充值 11购物赠送 20消费 31转账-转入 32转账-转出")
    private WalletBillTypeEnum type;

    @Schema(description = "钱包入账类型 10余额 20代币")
    private WalletBillWalletTypeEnum walletType;

    @Schema(description = "账单金额。正数-入账；负数-出账")
    private BigDecimal amount;

    @Schema(description = "账单时间")
    private Long createdAt;

    @Schema(description = "type=11,20时，相关订单id")
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orderId;

    @Schema(description = "type=31,32时，相关转账记录id")
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long walletTransferId;

    @Schema(description = "当前账单发生在用户所在的场次id")
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long sessionUserId;

    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long sessionId;
}
