package com.pighand.aio.vo.MKT;

import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.MKT.LotteryCommonUserDomain;
import com.pighand.aio.domain.MKT.LotteryParticipatePrizeDomain;
import com.pighand.aio.domain.base.UserDomain;
import com.pighand.aio.domain.base.UserExtensionDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 营销 - 抽奖参与用户
 *
 * @author wangshuli
 * @createDate 2024-11-28 17:21:35
 */
@Data
@TableRef(LotteryCommonUserDomain.class)
@EqualsAndHashCode(callSuper = false)
public class LotteryCommonUserVO extends LotteryCommonUserDomain {

    // relation table: begin
    private UserExtensionDomain userExtension;

    private UserDomain user;

    private LotteryParticipatePrizeDomain prize;
    // relation table: end

    /**
     * 是否按中奖排序
     */
    private Boolean isOrderPrize;
}
