package com.pighand.aio.domain.ECommerce;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import com.pighand.framework.spring.base.BaseDomainRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * 电商 - 场次模板 - 按周期
 *
 * @author wangshuli
 * @createDate 2024-05-23 15:01:58
 */
@Table("ec_session_template_cycle")
@Data
@EqualsAndHashCode(callSuper = false)
public class SessionTemplateCycleDomain extends BaseDomainRecord<SessionTemplateCycleDomain> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("sessionTemplateCycleCreate")
    @RequestFieldException("sessionTemplateCycleUpdate")
    private Long id;

    private Long sessionTemplateId;

    @Schema(description = "周期类型 10星期 20月")
    private Integer cycleType;

    @Column("week")
    @Schema(description = "星期数")
    private Integer week;

    @Column("day")
    @Schema(description = "日期数")
    private Integer day;

    @Schema(description = "场次开始时间")
    private LocalTime beginTime;

    @Schema(description = "场次结束时间")
    private LocalTime endTime;
}
