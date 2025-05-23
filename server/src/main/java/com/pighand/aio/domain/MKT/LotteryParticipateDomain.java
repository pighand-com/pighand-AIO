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
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;

/**
 * 营销 - 抽奖 - 参与类型
 *
 * @author wangshuli
 * @createDate 2024-11-28 17:21:35
 */
@Table(value = "mkt_lottery_participate")
@Data
public class LotteryParticipateDomain extends BaseDomainRecord<LotteryParticipateDomain> implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    @RequestFieldException("mktLotteryParticipateCreate")
    @RequestFieldException("mktLotteryParticipateUpdate")
    private Long id;

    private Long lotteryCommonConfigId;

    @Length(max = 255)
    @Schema(description = "参与抽奖url")
    private String buttonParticipateUrl;

    @Length(max = 255)
    @Schema(description = "助力并参与url")
    private String buttonHelpParticipateUrl;

    @Length(max = 255)
    @Schema(description = "助力url")
    private String buttonHelpUrl;

    @Length(max = 255)
    @Schema(description = "邀请助力url")
    private String buttonInviteUrl;
}
