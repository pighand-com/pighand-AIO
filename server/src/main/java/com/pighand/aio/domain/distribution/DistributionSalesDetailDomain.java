package com.pighand.aio.domain.distribution;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import com.pighand.framework.spring.base.BaseDomainRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 分销 - 销售明细记录
 *
 * @author wangshuli
 * @createDate 2025-05-24 14:35:05
 */
@Table(value = "dist_distribution_sales_detail")
@Data
@EqualsAndHashCode(callSuper = false)
public class DistributionSalesDetailDomain extends BaseDomainRecord<DistributionSalesDetailDomain>
    implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("distDistributionSalesDetailCreate")
    @RequestFieldException("distDistributionSalesDetailUpdate")
    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "销售ID")
    private Long salespersonId;

    @Schema(description = "销售记录ID")
    private Long distributionSalesId;

    @Schema(description = "结算id")
    private Long withdrawDistributionSalesId;

    @Column("object_type")
    @Schema(description = "类型 10-主题 20-票务")
    private Integer objectType;

    @Schema(description = "对象ID")
    private Long objectId;

    @Schema(description = "金额")
    private BigDecimal amount;

    @Schema(description = "状态：0=冻结中 10=待结算，20=已结算, 90=订单退款")
    private Integer status;

    @Schema(description = "可结算时间")
    private Long settlementTime;

}