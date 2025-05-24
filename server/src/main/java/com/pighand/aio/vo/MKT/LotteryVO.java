package com.pighand.aio.vo.MKT;

import com.pighand.aio.domain.MKT.LotteryCommonConfigDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 营销 - 抽奖配置
 *
 * @author wangshuli
 * @createDate 2024-11-28 17:21:35
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LotteryVO extends LotteryCommonConfigDomain {

    /**
     * 参与类型活动
     */
    private LotteryParticipateVO participate;
}
