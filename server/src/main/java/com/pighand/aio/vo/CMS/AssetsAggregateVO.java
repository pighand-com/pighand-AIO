package com.pighand.aio.vo.CMS;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.pighand.aio.domain.CMS.AssetsClassificationDomain;
import com.pighand.framework.spring.base.BaseDomain;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * CMS - 素材聚合查询VO
 * 用于统一查询文档、图片、视频三种素材类型的聚合结果
 *
 * @author wangshuli
 * @createDate 2025-01-21
 */
@Data
public class AssetsAggregateVO extends BaseDomain {

    /**
     * 素材ID
     */
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 应用ID
     */
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long applicationId;

    /**
     * 分类ID
     */
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long classificationId;

    /**
     * 素材类型：doc-文档，image-图片，video-视频
     */
    private String assetType;

    /**
     * 标题
     */
    private String title;

    /**
     * 描述
     */
    private String description;

    /**
     * 封面图URL
     */
    private String coverUrl;

    /**
     * 文件URL（文档/图片/视频的实际文件地址）
     */
    private String fileUrl;

    /**
     * 文件格式
     */
    private String fileFormat;

    /**
     * 文件大小（字节）
     */
    private Long fileSize;

    /**
     * 浏览量
     */
    private Integer viewCount;

    /**
     * 下载量
     */
    private Integer downloadCount;

    /**
     * 是否精选
     */
    private Boolean handpick;

    /**
     * 状态：10-上架，20-下架
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * 更新时间
     */
    private Date updatedAt;

    /**
     * 创建者ID
     */
    private Long createdBy;

    /**
     * 更新者ID
     */
    private Long updatedBy;

    // 关联信息

    /**
     * 分类信息
     */
    private AssetsClassificationDomain classification;

    /**
     * 专辑相关信息
     */
    private List<AssetsCollectionRelevanceVO> collections;

    // 查询条件字段（分页信息和关联表名已在BaseDomain中定义）

    /**
     * 专辑ID列表（用于查询条件）
     */
    private List<Long> collectionIds;

    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long collectionId;

    /**
     * 素材类型列表（用于查询条件，可指定查询哪些类型的素材）
     */
    private List<String> assetTypes;

    /**
     * 关键词搜索（标题、描述）
     */
    private String keyword;

    /**
     * 状态列表（用于查询条件）
     */
    private List<Integer> statusList;

    /**
     * 是否只查询精选
     */
    private Boolean onlyHandpick;

    /**
     * 开始时间（创建时间范围查询）
     */
    private Date startTime;

    /**
     * 结束时间（创建时间范围查询）
     */
    private Date endTime;

    /**
     * 排序方式：download-按下载量倒序，其他值或空值按id倒序
     */
    private String sort;
}