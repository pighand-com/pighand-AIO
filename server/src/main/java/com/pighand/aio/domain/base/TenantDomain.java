package com.pighand.aio.domain.base;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import com.pighand.framework.spring.base.BaseDomainRecordTs;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 租户
 *
 * @author wangshuli
 * @createDate 2024-12-31 19:04:50
 */
@Table(value = "base_tenant")
@EqualsAndHashCode(callSuper = false)
@Data
public class TenantDomain extends BaseDomainRecordTs<TenantDomain> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("baseTenantCreate")
    @RequestFieldException("baseTenantUpdate")
    private Long id;

    private Long applicationId;

    @Column("name")
    @Length(max = 32)
    private String name;
}
