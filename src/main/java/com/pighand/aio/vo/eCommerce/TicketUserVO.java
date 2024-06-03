package com.pighand.aio.vo.ECommerce;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.pighand.aio.domain.ECommerce.TicketUserDomain;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import lombok.Data;

import java.util.List;

/**
 * 电商 - 已购票务
 *
 * @author wangshuli
 * @createDate 2024-04-26 14:52:18
 */
@Data
public class TicketUserVO extends TicketUserDomain {

    // relation table: begin
    // relation table: end

    // 适用信息
    List<TicketUserValidityVO> validity;
    // 查询可用票务
    private Boolean usable;
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long deviceId;
    private String message;
}
