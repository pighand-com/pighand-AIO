package com.pighand.aio.domain.CMS;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.pighand.aio.common.enums.ArticleTypeEnum;
import com.pighand.aio.common.enums.AssetsStatusEnum;
import com.pighand.aio.common.enums.ArticleTypeEnum;
import com.pighand.aio.common.enums.AssetsStatusEnum;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import com.pighand.framework.spring.base.BaseDomainRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;


/**
 * CMS - 文章
 *
 * @author wangshuli
 * @createDate 2024-04-22 15:11:06
 */
@Table("cms_article")
@Data
@EqualsAndHashCode(callSuper = false)
public class ArticleDomain extends BaseDomainRecord<ArticleDomain> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("articleCreate")
    @RequestFieldException("articleUpdate")
    private Long id;

    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long categoryId;

    @Length(max = 65535)
    private String bannerUrl;

    @Length(max = 32)
    private String title;

    @Column("type")
    @Schema(description = "类型 10图片 20视频 30文件")
    private ArticleTypeEnum type;

    @Length(max = 65535)
    private String fileUrl;

    @Column("description")
    @Length(max = 65535)
    private String description;

    @Column("status")
    @Schema(description = "状态 10上架 20下架")
    private AssetsStatusEnum status;

    private Long creatorId;

    private Long createdAt;

    @Schema(description = "浏览量")
    private Integer viewCount;

    @Schema(description = "下载量")
    private Integer downloadCount;

    @Schema(description = "删除")
    private Boolean deleted;
}
