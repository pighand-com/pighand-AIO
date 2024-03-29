package com.pighand.aio.domain.eCommerce;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import com.pighand.framework.spring.base.BaseDomain;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 电商 - 场次模板。根据模板生成场次
 *
 * @author wangshuli
 * @createDate 2023-12-05 16:13:27
 */
@Table(value = "session_template")
@Data
public class SessionTemplateDomain extends BaseDomain implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("sessionTemplateCreate")
    @RequestFieldException("sessionTemplateUpdate")
    private Long id;
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long projectId;
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long storeId;
    @Schema(description = "场次类型 10按用户分组")
    private Integer type;
    private Date beginTime;
    private Date endTime;
}
