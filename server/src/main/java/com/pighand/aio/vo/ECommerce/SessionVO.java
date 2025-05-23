package com.pighand.aio.vo.ECommerce;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.pighand.aio.domain.ECommerce.SessionDomain;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import lombok.Data;

import java.util.List;

@Data
public class SessionVO extends SessionDomain {

    private List<SessionUserGroupVO> sessionUserGroup;

    private List<SessionUserCycleVO> sessionUserCycle;

    private List<OrderSkuVO> orderSku;

    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long sessionGroupId;

    private List<SessionTemplateGourpVO> groups;

    private SessionTemplateVO sessionTemplate;
}
