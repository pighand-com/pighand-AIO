package com.pighand.aio.vo.CMS;

import com.pighand.aio.domain.CMS.AssetsDownloadDomain;
import lombok.Data;

/**
 * CMS - 素材 - 下载记录
 *
 * @author wangshuli
 * @createDate 2025-01-25 10:00:00
 */
@Data
public class AssetsDownloadVO extends AssetsDownloadDomain {

    // relation table: begin
    
    /**
     * 素材标题
     */
    private String title;
    
    /**
     * 素材描述
     */
    private String description;
    
    /**
     * 素材文件URL
     */
    private String url;
    
    /**
     * 素材封面图URL
     */
    private String coverUrl;
    
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
    
    // relation table: end
}