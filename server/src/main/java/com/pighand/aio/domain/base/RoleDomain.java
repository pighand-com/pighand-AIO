package com.pighand.aio.domain.base;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import com.pighand.framework.spring.base.BaseDomainRecord;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 角色
 *
 * @author wangshuli
 * @createDate 2025-06-04 10:08:01
 */
@Table(value = "base_role")
@Data
public class RoleDomain extends BaseDomainRecord<RoleDomain> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("baseRoleCreate")
    @RequestFieldException("baseRoleUpdate")
    private Long id;

    private Long applicationId;

    @Column("name")
    @Length(max = 255)
    private String name;
}
