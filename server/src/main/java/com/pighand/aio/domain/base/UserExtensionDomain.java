package com.pighand.aio.domain.base;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.api.jacksonSerializer.Conceal;
import com.pighand.framework.spring.base.BaseDomainRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户扩展信息。id与user表相同
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@Data
@Table("base_user_extension")
public class UserExtensionDomain extends BaseDomainRecord<UserExtensionDomain> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("userExtensionCreate")
    @RequestFieldException("userExtensionUpdate")
    private Long id;

    @NotNull(groups = {ValidationGroup.Create.class})
    @Schema(description = "应用id")
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long applicationId;

    @Length(max = 16)
    @Schema(description = "昵称")
    private String nickname;

    @Conceal(suffix = 2)
    @Length(max = 64)
    @Schema(description = "姓名")
    private String name;

    @Schema(description = "性别 1男 2女")
    private Integer gender;

    @Length(max = 255)
    @Schema(description = "头像url")
    private String profile;

    @Length(max = 3)
    @Schema(description = "星座")
    private String zodiacSign;

    @Length(max = 2)
    @Schema(description = "血型")
    private String bloodType;

    @Schema(description = "生日")
    private Date birthday;

    @Schema(description = "注册时间")
    private Date registerAt;

    @Schema(description = "最后登录时间")
    private Date lastSignInAt;

    @Schema(description = "是否实人认证")
    private Integer realPersonCertification;

    @Schema(description = "是否初始化信息。true: 信息来自于初始化或三方平台的信息；false：自行修改过")
    private Boolean initialInfo;
}
