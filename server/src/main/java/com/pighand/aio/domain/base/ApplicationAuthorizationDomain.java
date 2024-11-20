package com.pighand.aio.domain.base;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.handler.JacksonTypeHandler;
import com.pighand.aio.common.enums.AuthorizationAlgEnum;
import com.pighand.aio.common.enums.AuthorizationTypeEnum;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.api.springdoc.dataType.EmptyObject;
import com.pighand.framework.spring.base.BaseDomainRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Map;

/**
 * Authorization配置
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@Data
@Table("base_application_authorization")
public class ApplicationAuthorizationDomain extends BaseDomainRecord<ApplicationAuthorizationDomain>
    implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("projectAuthorizationUpdate")
    private Long id;

    @NotNull(groups = {ValidationGroup.Create.class})
    @Schema(description = "类型 1-jwt 2-hash")
    private AuthorizationTypeEnum type;

    @NotNull(groups = {ValidationGroup.Create.class})
    @Schema(description = "是否区分环境")
    private Boolean matchingEnv;

    @NotNull(groups = {ValidationGroup.Create.class})
    @Schema(description = "是否支持退出登录")
    private Boolean supportLogout;

    @Length(max = 64)
    @Schema(description = "jwt盐")
    private String jwtSalt;

    @Schema(description = "Jwt-body格式。key：参数名；value：“表名.字段名”。", implementation = EmptyObject.class)
    @Column(typeHandler = JacksonTypeHandler.class)
    private Map<String, Object> jwtBody;

    @Length(max = 8)
    @Schema(description = "token前缀")
    private String hashPrefix;

    @NotNull(groups = {ValidationGroup.Create.class})
    @Schema(description = "签名算法")
    private AuthorizationAlgEnum alg;

    @NotNull(groups = {ValidationGroup.Create.class})
    @Schema(description = "过期时间，单位秒")
    private Integer expiresIn;
}
