package com.pighand.aio.domain.ECommerce;

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
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 电商 - 主题
 *
 * @author wangshuli
 * @createDate 2024-05-23 15:01:58
 */
@Table("ec_theme")
@Data
@EqualsAndHashCode(callSuper = false)
public class ThemeDomain extends BaseDomainRecord<ThemeDomain> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("themeCreate")
    @RequestFieldException("themeUpdate")
    private Long id;

    private Long applicationId;

    private Long storeId;

    @Length(max = 32)
    @Schema(description = "主题名称")
    private String themeName;

    @Length(max = 65535)
    @Schema(description = "主题简介")
    private String themeIntroductione;

    @Length(max = 255)
    @Schema(description = "主题封面")
    private String posterUrl;

    @Length(max = 65535)
    @Schema(description = "主题描述")
    private String pictureDescription;

    @Schema(description = "原价（分）")
    private Long originalPrice;

    @Schema(description = "现价（分）")
    private Long presentPrice;

    @Schema(description = "最少人数/起玩人数")
    private Integer minPeople;

    @Schema(description = "最大人数")
    private Integer maxPeople;

    @Schema(description = "时长(分钟)")
    private Integer duration;
}
