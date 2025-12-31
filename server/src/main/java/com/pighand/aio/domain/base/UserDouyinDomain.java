package com.pighand.aio.domain.base;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.pighand.aio.common.enums.DouyinSourcePlatformEnum;
import com.pighand.aio.common.enums.UserDouyinStatusEnum;
import com.pighand.aio.common.enums.DouyinSourcePlatformEnum;
import com.pighand.aio.common.enums.UserDouyinStatusEnum;
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
 * 用户 - 抖音
 *
 * @author wangshuli
 * @createDate 2024-06-05 23:58:27
 */
@Table("base_user_douyin")
@Data
@EqualsAndHashCode(callSuper = false)
public class UserDouyinDomain extends BaseDomainRecord<UserDouyinDomain> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("userDouyinCreate")
    @RequestFieldException("userDouyinUpdate")
    private Long id;

    @NotNull(groups = {ValidationGroup.Create.class})
    @Schema(description = "应用id")
    private Long applicationId;

    @NotNull(groups = {ValidationGroup.Create.class})
    @Schema(description = "用户id")
    private Long userId;

    @Length(max = 40)
    private String openid;

    @Length(max = 40)
    private String unionid;

    @Length(max = 40)
    private String anonymousOpenid;

    @Schema(description = "来源平台 32抖音小程序 33抖音小游戏")
    private Integer sourcePlatform;

    @Column("status")
    @Schema(description = "状态 10正常 20停用")
    private Integer status;
}
