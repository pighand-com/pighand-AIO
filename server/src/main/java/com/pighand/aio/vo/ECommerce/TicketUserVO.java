package com.pighand.aio.vo.ECommerce;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.ECommerce.ThemeDomain;
import com.pighand.aio.domain.ECommerce.TicketDomain;
import com.pighand.aio.domain.ECommerce.TicketUserDomain;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 电商 - 已购票务
 *
 * @author wangshuli
 * @createDate 2024-04-26 14:52:18
 */
@Data
@TableRef(TicketUserDomain.class)
@EqualsAndHashCode(callSuper = false)
public class TicketUserVO extends TicketUserDomain {

    // 适用信息
    List<TicketUserValidityVO> validity;
    // relation table: end

    // relation table: begin
    private TicketDomain ticket;

    private ThemeDomain theme;

    // 查询可用票务
    private Boolean usable;

    // 查询已核销票务
    private Boolean used;

    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long deviceId;

    private String message;

    private Integer validationCount;
}
