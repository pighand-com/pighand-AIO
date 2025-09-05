package com.pighand.aio.domain.CMS;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import com.pighand.framework.spring.base.BaseDomainRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;

/**
 * CMS - 素材 - 分类
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@Table(value = "cms_assets_classification")
@Data
public class AssetsClassificationDomain extends BaseDomainRecord<AssetsClassificationDomain> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("cmsAssetsClassificationCreate")
    @RequestFieldException("cmsAssetsClassificationUpdate")
    private Long id;

    private Long parentId;

    @Column("name")
    @Length(max = 16)
    private String name;

    @Schema(description = "是否系统内置")
    private Integer inlay;

    @Schema(description = "创建人")
    private Long createdBy;

    @Schema(description = "创建时间")
    private Date createdAt;

    @Schema(description = "更新时间")
    private Date updatedAt;

    @Schema(description = "是否删除")
    private Integer deleted;
}
