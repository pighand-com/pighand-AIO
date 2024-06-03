package com.pighand.aio.domain.ECommerce;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import com.pighand.framework.spring.base.BaseDomain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 电商 - 场次 - 用户周期
 *
 * @author wangshuli
 * @createDate 2024-05-23 15:01:58
 */
@Table(value = "session_user_cycle")
@Data
public class SessionUserCycleDomain extends BaseDomain implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("sessionUserCycleCreate")
    @RequestFieldException("sessionUserCycleUpdate")
    private Long id;
    private Long sessionId;
    private Long sessionCycleId;
    private Long orderId;
    private Long userId;
    private Date createdAt;
}
