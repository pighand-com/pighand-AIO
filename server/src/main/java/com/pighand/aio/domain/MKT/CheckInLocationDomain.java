package com.pighand.aio.domain.MKT;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.handler.JacksonTypeHandler;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import com.pighand.framework.spring.base.BaseDomainRecordTs;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.List;

/**
 * 营销 - 打卡地点
 *
 * @author wangshuli
 * @createDate 2024-12-30
 */
@Table(value = "mkt_check_in_location")
@Data
@EqualsAndHashCode(callSuper = false)
public class CheckInLocationDomain extends BaseDomainRecordTs<CheckInLocationDomain> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("checkInLocationCreate")
    @RequestFieldException("checkInLocationUpdate")
    private Long id;

    @Column(typeHandler = JacksonTypeHandler.class)
    private List<Long> parents;

    @Schema(description = "应用id")
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long applicationId;

    @Schema(description = "门店id")
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long storeId;

    @Length(max = 255)
    @Schema(description = "打卡点名称")
    private String name;

}