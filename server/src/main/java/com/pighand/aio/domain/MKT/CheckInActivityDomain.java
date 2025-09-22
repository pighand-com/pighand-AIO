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
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 营销 - 打卡活动
 *
 * @author wangshuli
 * @createDate 2024-12-30
 */
@Table(value = "mkt_check_in_activity")
@Data
@EqualsAndHashCode(callSuper = false)
public class CheckInActivityDomain extends BaseDomainRecord<CheckInActivityDomain> implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("checkInActivityCreate")
    @RequestFieldException("checkInActivityUpdate")
    private Long id;

    /**
     * 活动名称
     */
    @Length(max = 32)
    @Schema(description = "活动名称")
    private String name;

    /**
     * 时长，单位分
     */
    @Schema(description = "时长，单位分")
    private Long time;

}