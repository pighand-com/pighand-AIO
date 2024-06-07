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
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 电商 - 场次模板 - 分组
 *
 * @author wangshuli
 * @createDate 2023-12-05 16:13:27
 */
@Table("ec_session_template_gourp")
@Data
public class SessionTemplateGourpDomain extends BaseDomainRecord<SessionTemplateGourpDomain> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("sessionTemplateGourpCreate")
    @RequestFieldException("sessionTemplateGourpUpdate")
    private Long id;
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long sessionTemplateId;
    @Schema(description = "分组类型")
    private Integer type;
    @Length(max = 16)
    @Schema(description = "分组名")
    private String name;
    @Length(max = 255)
    @Schema(description = "组logo")
    private String logo;
}
