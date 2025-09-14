package com.pighand.aio.domain.CMS;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import com.pighand.framework.spring.base.BaseDomainRecordTs;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;

/**
 * CMS - 素材 - 视频
 *
 * @author wangshuli
 * @createDate 2025-09-03 17:16:47
 */
@Table(value = "cms_assets_video")
@Data
public class AssetsVideoDomain extends BaseDomainRecordTs<AssetsVideoDomain> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("cmsAssetsVideoCreate")
    @RequestFieldException("cmsAssetsVideoUpdate")
    private Long id;

    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long applicationId;

    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @Schema(description = "分类id")
    private Long classificationId;

    @Length(max = 32)
    @Schema(description = "标题")
    private String title;

    @Column("description")
    @Length(max = 200)
    @Schema(description = "描述")
    private String description;

    @Length(max = 255)
    @Schema(description = "封面图url")
    private String coverUrl;

    @Column("url")
    @Length(max = 255)
    private String url;

    @Length(max = 32)
    @Schema(description = "文件格式")
    private String fileFormat;

    @Schema(description = "文件大小（B）")
    private Long fileSize;

    @Schema(description = "分辨率，格式 Height x Width")
    private String resolutionRatio;

    @Schema(description = "浏览量")
    private Integer viewCount;

    @Schema(description = "下载量")
    private Integer downloadCount;

    @Schema(description = "是否精选")
    private Boolean handpick;

    @Schema(description = "状态：10-上架，20-下架")
    private Integer status;

    @Schema(description = "创建人")
    private Long createdBy;

    @Schema(description = "创建时间")
    private Date createdAt;

    @Schema(description = "更新时间")
    private Date updatedAt;

    @Schema(description = "是否删除")
    private Boolean deleted;
}
