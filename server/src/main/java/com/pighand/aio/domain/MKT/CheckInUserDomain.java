package com.pighand.aio.domain.MKT;

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
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 营销 - 打卡用户参与信息
 *
 * @author wangshuli
 * @createDate 2024-12-30
 */
@Table(value = "mkt_check_in_user")
@Data
@EqualsAndHashCode(callSuper = false)
public class CheckInUserDomain extends BaseDomainRecord<CheckInUserDomain> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("checkInUserCreate")
    @RequestFieldException("checkInUserUpdate")
    private Long id;

    @Schema(description = "用户id")
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @Schema(description = "截止时间")
    private LocalDateTime endTime;
}