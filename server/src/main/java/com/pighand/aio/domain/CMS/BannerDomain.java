package com.pighand.aio.domain.CMS;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import com.pighand.framework.spring.base.BaseDomainRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * CMS - banner
 *
 * @author wangshuli
 * @createDate 2025-06-17 13:59:52
 */
@Table(value = "cms_banner")
@Data
public class BannerDomain extends BaseDomainRecord<BannerDomain> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("cmsBannerCreate")
    @RequestFieldException("cmsBannerUpdate")
    private Long id;

    private Long applicationId;

    @Length(max = 500)
    @Schema(description = "描述")
    private String description;

    @Length(max = 255)
    @Schema(description = "图片地址")
    private String imageUrl;

    @Length(max = 255)
    @Schema(description = "跳转地址")
    private String redirectionPath;

    @Schema(description = "状态：0下架，10上架")
    private Integer status;
}
