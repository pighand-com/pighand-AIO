package com.pighand.aio.domain.MKT;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;
import com.pighand.framework.spring.api.annotation.field.RequestFieldException;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseDomainRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 营销 - 抽奖 - 参与类型 - 奖品
 *
 * @author wangshuli
 * @createDate 2024-11-28 17:21:35
 */
@Table(value = "mkt_lottery_participate_prize")
@Data
public class LotteryParticipatePrizeDomain extends BaseDomainRecord<LotteryParticipatePrizeDomain>
    implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("mktLotteryParticipatePrizeCreate")
    @RequestFieldException("mktLotteryParticipatePrizeUpdate")
    private Long id;
    @NotNull(groups = {ValidationGroup.Create.class})
    private Long lotteryParticipateId;
    @Column("name")
    @NotNull(groups = {ValidationGroup.Create.class})
    @Length(max = 16)
    @Schema(description = "奖品名称")
    private String name;
    @Column("description")
    @Length(max = 32)
    @Schema(description = "简介")
    private String description;
    @Length(max = 255)
    @Schema(description = "奖品图片")
    private String imageUrl;
    @NotNull(groups = {ValidationGroup.Create.class})
    @Schema(description = "中奖名额")
    private Integer lotteryQuota;
}
