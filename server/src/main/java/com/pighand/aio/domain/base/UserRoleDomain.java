package com.pighand.aio.domain.base;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseDomainRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 用户角色关联表
 *
 * @author wangshuli
 * @createDate 2024-01-01 00:00:00
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Table("base_user_role")
public class UserRoleDomain extends BaseDomainRecord<UserRoleDomain> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("userRoleCreate")
    @RequestFieldException("userRoleUpdate")
    private Long id;

    @NotNull(groups = {ValidationGroup.Create.class})
    @Schema(description = "用户ID")
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    @NotNull(groups = {ValidationGroup.Create.class})
    @Schema(description = "角色ID")
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long roleId;

}