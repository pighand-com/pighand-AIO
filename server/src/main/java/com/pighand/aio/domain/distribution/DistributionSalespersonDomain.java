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

import java.io.Serializable;

/**
 * 分销 - 销售资格
 *
 * @author wangshuli
 * @createDate 2025-05-24 14:35:05
 */
@Table(value = "dist_distribution_salesperson")
@Data
public class DistributionSalespersonDomain extends BaseDomainRecord<DistributionSalespersonDomain>
    implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("distDistributionSalespersonCreate")
    @RequestFieldException("distDistributionSalespersonUpdate")
    @Schema(description = "主键ID")
    private Long id;

    @NotNull(groups = {ValidationGroup.Create.class})
    @Schema(description = "用户ID，具备销售资格的用户")
    private Long userId;

    @Column("status")
    @NotNull(groups = {ValidationGroup.Create.class})
    @Schema(description = "状态：1=启用，0=禁用")
    private Integer status;
}
