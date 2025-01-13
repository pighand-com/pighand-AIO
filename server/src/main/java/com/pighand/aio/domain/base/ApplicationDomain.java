package com.pighand.aio.domain.base;

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
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 应用
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@Table(value = "base_application")
@Data
public class ApplicationDomain extends BaseDomainRecord<ApplicationDomain> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("projectCreate")
    @RequestFieldException("projectUpdate")
    private Long id;

    @Length(max = 16)
    private String name;

    @Schema(description = "系统默认加密盐。用于加密二维码等。初始化时由系统生成")
    private String systemEncryptSalt;
}
