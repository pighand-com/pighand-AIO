package com.pighand.aio.domain.base;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.pighand.aio.common.enums.PlatformEnum;
import com.pighand.aio.common.enums.UserStatusEnum;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import com.pighand.framework.spring.base.BaseDomainRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 用户关键信息表，主要保存登录所用信息
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Table("base_user")
public class UserDomain extends BaseDomainRecord<UserDomain> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("userCreate")
    @RequestFieldException("userUpdate")
    private Long id;

    @Schema(description = "所属应用")
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long applicationId;

    @Schema(description = "所属租户")
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long tenantId;

    @Schema(description = "所属门店")
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long storeId;

    @Schema(description = "角色id")
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long roleId;

    @Length(max = 255)
    @Schema(description = "密码")
    private String password;

    @Length(max = 32)
    @Schema(description = "用户名")
    private String username;

    @Length(max = 32)
    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "邮箱是否已验证")
    private Boolean emailVerified;

    @Schema(description = "手机号区号")
    private Integer phoneArea;

    @Length(max = 24)
    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "手机号是否已验证")
    private Boolean phoneVerified;

    @Schema(description = "初始来源平台")
    private PlatformEnum initialSourcePlatform;

    @Schema(description = "状态 10正常 20停用")
    private UserStatusEnum status;

}
