package com.pighand.aio.domain.ECommerce;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.pighand.aio.common.enums.CouponUserStatusEnum;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import com.pighand.framework.spring.base.BaseDomainRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;


/**
 * 电商 - 优惠券 - 用户已领
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Table("ec_coupon_user")
@Data
@EqualsAndHashCode(callSuper = false)
public class CouponUserDomain extends BaseDomainRecord<CouponUserDomain> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("couponUserCreate")
    @RequestFieldException("couponUserUpdate")
    private Long id;

    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long couponId;

    @Schema(description = "状态 10未使用 20商户核销")
    private CouponUserStatusEnum status;

    @Schema(description = "使用订单id")
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long usedOrderId;

    @Schema(description = "使用时间")
    private Long usedAt;

    @Schema(description = "发券时间")
    private Long createdAt;

    @Schema(description = "发券人")
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long creatorId;

    @Schema(description = "持有人")
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long ownerId;
}
