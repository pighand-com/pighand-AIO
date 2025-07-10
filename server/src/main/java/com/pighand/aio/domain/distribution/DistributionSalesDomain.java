package com.pighand.aio.domain.distribution;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseDomainRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 分销 - 销售记录
 *
 * @author wangshuli
 * @createDate 2025-05-24 14:35:05
 */
@Table(value = "dist_distribution_sales")
@Data
@EqualsAndHashCode(callSuper = false)
public class DistributionSalesDomain extends BaseDomainRecord<DistributionSalesDomain> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("distDistributionSalesCreate")
    @RequestFieldException("distDistributionSalesUpdate")
    @Schema(description = "主键ID")
    private Long id;

    @NotNull(groups = {ValidationGroup.Create.class})
    @Schema(description = "销售ID")
    private Long salespersonId;

    @Schema(description = "订单ID")
    private Long orderId;

    @Schema(description = "结算单id")
    private Long settlementId;

    @Schema(description = "冻结金额")
    private BigDecimal frozenAmount;

    @Schema(description = "结算金额")
    private BigDecimal settledAmount;

    @Schema(description = "退款金额")
    private BigDecimal refundAmount;

    @Column("type")
    @Schema(description = "类型 10-销售单 20-结算单")
    private Integer type;

    private Date createdAt;
}
