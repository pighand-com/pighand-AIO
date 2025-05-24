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
 * 电商 - 订单
 *
 * @author wangshuli
 * @createDate 2024-04-18 14:35:34
 */
@Table("ec_order")
@Data
@EqualsAndHashCode(callSuper = false)
public class OrderDomain extends BaseDomainRecord<OrderDomain> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("orderCreate")
    @RequestFieldException("orderUpdate")
    private Long id;

    private Long applicationId;

    @Schema(description = "商户id")
    private Long businessId;

    @Schema(description = "门店id")
    private Long storeId;

    @Length(max = 32)
    @Schema(description = "交易单id")
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long orderTradeId;

    @Length(max = 32)
    @Schema(description = "订单号")
    private String sn;

    @Length(max = 255)
    @Schema(description = "订单备注")
    private String remark;

    @Schema(description = "交易状态 10待支付 20已支付/代发货/部分发货 30全部已发货/待收货 40确认收货 50超时取消 51全部退款")
    private Integer tradeStatus;

    @Schema(description = "退款状态 10不可退款 11可退款 20退款申请 30退款中 40已退款")
    private Integer refundStatus;

    @Schema(description = "评价状态 10不可评价 20待评价 30已评价")
    private Integer evaluationStatus;

    @Schema(description = "应付金额（分）")
    private Long amountPayable;

    @Schema(description = "实付金额（分）")
    private Long amountPaid;

    @Length(max = 32)
    @Schema(description = "收件人姓名")
    private String userName;

    @Length(max = 255)
    @Schema(description = "收件地址")
    private String userAddress;

    @Length(max = 18)
    @Schema(description = "收件人手机")
    private String userPhone;

    @Schema(description = "创建时间")
    private Date createdAt;

    @Length(max = 32)
    @Schema(description = "创建人")
    private Long creatorId;
}
