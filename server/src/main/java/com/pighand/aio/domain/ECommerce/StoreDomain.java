package com.pighand.aio.domain.ECommerce;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import com.pighand.framework.spring.base.BaseDomainRecord;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 电商 - 门店
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Table("ec_store")
@Data
public class StoreDomain extends BaseDomainRecord<StoreDomain> implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("storeCreate")
    @RequestFieldException("storeUpdate")
    private Long id;
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long applicationId;
    @Length(max = 16)
    private String name;
}
