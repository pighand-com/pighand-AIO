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
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 电商 - 场次模板。根据模板生成场次
 *
 * @author wangshuli
 * @createDate 2023-12-05 16:13:27
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Table("ec_session_template")
public class SessionTemplateDomain extends BaseDomainRecord<SessionTemplateDomain> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("sessionTemplateCreate")
    @RequestFieldException("sessionTemplateUpdate")
    private Long id;

    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long applicationId;

    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long storeId;

    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long themeId;

    @Schema(description = "场次类型 10按用户分组")
    private Integer type;

    @Schema(description = "最少人数/起玩人数")
    private Integer minPeople;

    @Schema(description = "最大人数")
    private Integer maxPeople;

    @Schema(description = "是否支持包场")
    private Boolean bookingPrivate;

    @Schema(description = "是否支持拼场")
    private Boolean bookingShared;

    @Schema(description = "拼场成功后，是否可以继续加入")
    private Boolean continueJoin;
}
