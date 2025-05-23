package com.pighand.aio.domain.system;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import com.pighand.framework.spring.base.BaseDomain;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 简单定时器
 *
 * @author wangshuli
 * @createDate 2024-04-16 11:44:49
 */
@Table("sys_simple_scheduling")
@Data
public class SimpleSchedulingDomain extends BaseDomain implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("simpleSchedulingCreate")
    @RequestFieldException("simpleSchedulingUpdate")
    private Long id;

    @Length(max = 24)
    private String corn;

    @Column("enable")
    @Schema(description = "是否启动")
    private Boolean enable;

    @Length(max = 255)
    @Schema(description = "调用的方法名")
    private String functionName;

    @Schema(description = "描述")
    private String description;
}
