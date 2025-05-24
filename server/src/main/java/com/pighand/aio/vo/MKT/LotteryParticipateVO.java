package com.pighand.aio.vo.MKT;

import com.pighand.aio.domain.MKT.LotteryParticipateDomain;
import com.pighand.aio.domain.MKT.LotteryParticipatePrizeDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 营销 - 抽奖 - 参与类型
 *
 * @author wangshuli
 * @createDate 2024-11-28 17:21:35
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class LotteryParticipateVO extends LotteryParticipateDomain {

    /**
     * 奖品配置
     */
    private List<LotteryParticipatePrizeDomain> prizes;
}
