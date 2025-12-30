package com.pighand.aio.domain.MKT;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.handler.JacksonTypeHandler;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import com.pighand.framework.spring.api.springdoc.dataType.EmptyObject;
import com.pighand.framework.spring.base.BaseDomainRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 营销 - 抽奖配置
 *
 * @author wangshuli
 * @createDate 2024-11-28 17:21:35
 */
@Table(value = "mkt_lottery_common_config")
@Data
@EqualsAndHashCode(callSuper = false)
public class LotteryCommonConfigDomain extends BaseDomainRecord<LotteryCommonConfigDomain> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("mktLotteryCommonConfigCreate")
    @RequestFieldException("mktLotteryCommonConfigUpdate")
    private Long id;

    @RequestFieldException("mktLotteryCommonConfigCreate")
    @RequestFieldException("mktLotteryCommonConfigUpdate")
    private Long storeId;

    @Schema(description = "抽奖类型 10参与型抽奖")
    private Integer lotteryType;

    @Length(max = 32)
    @Schema(description = "标题")
    private String title;

    @Schema(description = "描述")
    private String description;

    @Length(max = 255)
    @Schema(description = "封面图")
    private String coverUrl;

    @Schema(description = "开始时间")
    private Long beginTime;

    @Schema(description = "结束时间")
    private Long endTime;

    @Schema(description = "开奖时间")
    private Long drawTime;

    @Schema(description = "开奖状态 10未开奖 20开奖中 30开奖完成")
    private Integer drawStatus;

    @Length(max = 255)
    @Schema(description = "分享图片url")
    private String shareImageUrl;

    @Schema(description = "背景图片", implementation = EmptyObject.class)
    @Column(typeHandler = JacksonTypeHandler.class)
    private List<String> backgroundUrls;

    @Length(max = 255)
    @Schema(description = "助力海报图url")
    private String helpPosterUrl;

    @Schema(description = "根据助力增加次数（每次助力增加x次）")
    private Integer helpAddByTimes;

    @Schema(description = "根据配置增加次数", implementation = EmptyObject.class)
    @Column(typeHandler = JacksonTypeHandler.class)
    private List<String> helpAddByConfig;

    @Schema(description = "最多被助力的次数")
    private Integer maxHelpMe;

    @Schema(description = "最多助力别人的次数")
    private Integer maxHelpOther;

    @Schema(description = "是否显示结果")
    private Integer showResult;

    @Length(max = 255)
    @Schema(description = "更多按钮图片url")
    private String moreButtonUrl;

    @Length(max = 255)
    @Schema(description = "更多信息外部连接连接")
    private String moreExternalUrl;

    @Length(max = 255)
    @Schema(description = "结果通知url。小程序页面或url")
    private String resultNotifyUrl;
}
