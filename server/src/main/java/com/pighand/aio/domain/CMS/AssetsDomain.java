package com.pighand.aio.domain.CMS;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.pighand.aio.common.base.ApplicationIdAware;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import com.pighand.framework.spring.base.BaseDomainRecordTs;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 公共 - 素材
 *
 * @author wangshuli
 * @createDate 2024-12-31 19:04:50
 */
@Table(value = "com_assets")
@Data
@EqualsAndHashCode(callSuper = false)
public class AssetsDomain extends BaseDomainRecordTs<AssetsDomain> implements Serializable, ApplicationIdAware {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("comAssetsCreate")
    @RequestFieldException("comAssetsUpdate")
    private Long id;

    private Long applicationId;

    @Length(max = 32)
    private String tag;

    @Column("url")
    @Length(max = 255)
    private String url;
}
