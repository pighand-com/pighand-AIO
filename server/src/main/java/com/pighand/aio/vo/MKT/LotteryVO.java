package com.pighand.aio.vo.MKT;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.MKT.LotteryCommonConfigDomain;
import com.pighand.framework.spring.api.annotation.serialization.ToLongSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 营销 - 抽奖配置
 *
 * @author wangshuli
 * @createDate 2024-11-28 17:21:35
 */
@Data
@TableRef(LotteryCommonConfigDomain.class)
@EqualsAndHashCode(callSuper = false)
public class LotteryVO extends LotteryCommonConfigDomain {

    /**
     * 参与类型活动
     */
    private LotteryParticipateVO participate;

    /**
     * 活动是否开始
     */
    private Boolean isBegin;

    /**
     * 活动是否结束
     */
    private Boolean isEnd;

    /**
     * 是否参与
     */
    private Boolean isParticipate;

    /**
     * 用户获得奖品ID
     */
    @JsonDeserialize(using = ToLongSerializer.class)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userPrizeId;

    /**
     * 参与人数
     */
    private Long participateCount;

    /**
     * 是否查询结束的
     */
    private Boolean isQueryFinish;
}
