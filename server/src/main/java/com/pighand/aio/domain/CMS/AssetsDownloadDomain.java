package com.pighand.aio.domain.CMS;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseDomainRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * CMS - 素材 - 下载记录
 *
 * @author wangshuli
 * @createDate 2025-01-25 10:00:00
 */
@Table(value = "cms_assets_download")
@Data
public class AssetsDownloadDomain extends BaseDomainRecord<AssetsDownloadDomain> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("cmsAssetsDownloadCreate")
    @RequestFieldException("cmsAssetsDownloadUpdate")
    private Long id;

    @NotNull(groups = {ValidationGroup.Create.class})
    @Schema(description = "素材类型 10图片 20视频 30文档")
    private Integer assetsType;

    @NotNull(groups = {ValidationGroup.Create.class})
    @Schema(description = "素材id")
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long assetsId;

    @Schema(description = "下载用户ID")
    private Long createdBy;

    @Schema(description = "首次下载时间")
    private Date createdAt;

    @Schema(description = "最后下载时间")
    private Date updatedAt;
}