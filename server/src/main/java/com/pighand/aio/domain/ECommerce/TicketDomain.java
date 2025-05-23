package com.pighand.aio.domain.ECommerce;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 电商 - 票务
 *
 * @author wangshuli
 * @createDate 2024-04-26 14:52:18
 */
@Table("ec_ticket")
@Data
public class TicketDomain extends GoodsBaseInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("ticketCreate")
    @RequestFieldException("ticketUpdate")
    private Long id;

    private Long applicationId;

    @Schema(description = "商户id")
    private Long businessId;

    @Schema(description = "门店id")
    private Long storeId;

    @Column("name")
    @Length(max = 32)
    @Schema(description = "原价（分）")
    private Long originalPrice;

    @Schema(description = "现价（分）")
    private Long currentPrice;

    @Schema(description = "库存")
    private Integer stock;

    private String name;

    @Length(max = 65535)
    @Schema(description = "描述")
    private String details;

    @Schema(description = "核销次数")
    private Integer validationCount;

    private Integer deleted;
}
