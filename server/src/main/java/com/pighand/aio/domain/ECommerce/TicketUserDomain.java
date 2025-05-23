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

import java.io.Serializable;
import java.util.Date;

/**
 * 电商 - 已购票务
 *
 * @author wangshuli
 * @createDate 2024-04-26 14:52:18
 */
@Table("ec_ticket_user")
@Data
public class TicketUserDomain extends BaseDomainRecord<TicketUserDomain> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("ticketUserCreate")
    @RequestFieldException("ticketUserUpdate")
    private Long id;

    private Long ticketId;

    @Schema(description = "订单id")
    private Long orderId;

    @Schema(description = "剩余核销次数")
    private Integer remainingValidationCount;

    @Schema(description = "创建时间")
    private Date createdAt;

    @Schema(description = "创建人")
    private Long creatorId;

    @Schema(description = "核销时间")
    private Date validationAt;
}
