package com.pighand.aio.domain.MKT;

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
 * 营销 - 抽奖 - 参与型抽奖 - 奖品
 *
 * @author wangshuli
 * @createDate 2024-11-28 17:21:35
 */
@Table(value = "mkt_lottery_participate_prize")
@Data
@EqualsAndHashCode(callSuper = false)
public class LotteryParticipatePrizeDomain extends BaseDomainRecord<LotteryParticipatePrizeDomain>
    implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("mktLotteryParticipatePrizeCreate")
    @RequestFieldException("mktLotteryParticipatePrizeUpdate")
    private Long id;

    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long lotteryParticipateId;

    @Length(max = 16)
    @Schema(description = "奖品名称")
    private String name;

    @Length(max = 32)
    @Schema(description = "简介")
    private String description;

    @Length(max = 255)
    @Schema(description = "奖品图片")
    private String imageUrl;

    @Schema(description = "中奖名额")
    private Integer lotteryQuota;
}
