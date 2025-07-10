package com.pighand.aio.vo.MKT;

import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.MKT.LotteryCommonUserDomain;
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
    // relation table: end
}
