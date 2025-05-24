package com.pighand.aio.domain.MKT;

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
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 营销 - 抽奖参与用户
 *
 * @author wangshuli
 * @createDate 2024-11-28 17:21:35
 */
@Table(value = "mkt_lottery_common_user")
@Data
@EqualsAndHashCode(callSuper = false)
public class LotteryCommonUserDomain extends BaseDomainRecord<LotteryCommonUserDomain> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("mktLotteryCommonUserCreate")
    @RequestFieldException("mktLotteryCommonUserUpdate")
    private Long id;

    @NotNull(groups = {ValidationGroup.Create.class})
    private Long lotteryId;

    @NotNull(groups = {ValidationGroup.Create.class})
    private Long userId;

    @Schema(description = "奖品id")
    private Long prizeId;

    @Schema(description = "剩余参与次数")
    private Integer remainingParticipations;
}
