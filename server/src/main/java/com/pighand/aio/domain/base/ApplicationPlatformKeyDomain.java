package com.pighand.aio.domain.base;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.pighand.aio.common.enums.PlatformEnum;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseDomainRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 三方平台key
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Table("base_application_platform_key")
public class ApplicationPlatformKeyDomain extends BaseDomainRecord<ApplicationPlatformKeyDomain>
    implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("platformKeyCreate")
    @RequestFieldException("platformKeyUpdate")
    private Long id;

    @NotNull(groups = {ValidationGroup.Create.class})
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long applicationId;

    @NotNull(groups = {ValidationGroup.Create.class})
    @Schema(description = "三方平台类型")
    private PlatformEnum platform;

    @NotNull(groups = {ValidationGroup.Create.class})
    @Length(max = 255)
    private String appid;

    @NotNull(groups = {ValidationGroup.Create.class})
    @Length(max = 255)
    private String secret;

    private String region;

    private String bucket;
}
