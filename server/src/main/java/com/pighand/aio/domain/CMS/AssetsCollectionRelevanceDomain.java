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
 * CMS - 素材 - 专辑关系表
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@Table(value = "cms_assets_collection_relevance")
@Data
public class AssetsCollectionRelevanceDomain extends BaseDomainRecord<AssetsCollectionRelevanceDomain>
    implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("cmsAssetsCollectionRelevanceCreate")
    @RequestFieldException("cmsAssetsCollectionRelevanceUpdate")
    private Long id;

    @NotNull(groups = {ValidationGroup.Create.class})
    @Schema(description = "专辑(cms_assets_collection)id")
    private Long collectionId;

    @NotNull(groups = {ValidationGroup.Create.class})
    @Schema(description = "素材类型 10图片 20视频 30文档")
    private Integer assetsType;

    @NotNull(groups = {ValidationGroup.Create.class})
    @Schema(description = "素材id")
    private Long assetsId;

    @Schema(description = "创建人")
    private Long createdBy;

    @Schema(description = "创建时间")
    private Date createdAt;

    @Schema(description = "更新时间")
    private Date updatedAt;

    @Schema(description = "是否删除")
    private Integer deleted;
}
