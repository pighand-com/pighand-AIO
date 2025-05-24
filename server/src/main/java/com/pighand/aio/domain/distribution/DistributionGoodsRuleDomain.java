package com.pighand.aio.domain.distribution;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
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
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 分销 - 分销商品规则
 *
 * @author wangshuli
 * @createDate 2025-05-24 14:35:05
 */
@Table(value = "dist_distribution_goods_rule")
@Data
@EqualsAndHashCode(callSuper = false)
public class DistributionGoodsRuleDomain extends BaseDomainRecord<DistributionGoodsRuleDomain> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("distDistributionGoodsRuleCreate")
    @RequestFieldException("distDistributionGoodsRuleUpdate")
    @Schema(description = "主键ID")
    private Long id;

    @NotNull(groups = {ValidationGroup.Create.class})
    @Length(max = 20)
    @Schema(description = "对象类型，如ticket、theme")
    private String objectType;

    @NotNull(groups = {ValidationGroup.Create.class})
    @Schema(description = "对象ID")
    private Long objectId;

    @NotNull(groups = {ValidationGroup.Create.class})
    @Schema(description = "分成类型：10=比例，20=固定金额")
    private Integer sharingType;

    @Schema(description = "分成比例，仅比例模式使用")
    private BigDecimal sharingRatio;

    @Schema(description = "分成固定金额，仅固定金额模式使用")
    private BigDecimal sharingPrice;
}
