package com.pighand.aio.vo.ECommerce;

import com.pighand.aio.domain.ECommerce.SessionUserCycleDomain;
import com.pighand.aio.vo.base.UserVO;
import lombok.Data;

/**
 * 电商 - 场次 - 用户周期
 *
 * @author wangshuli
 * @createDate 2024-05-23 15:01:58
 */
@Data
public class SessionUserCycleVO extends SessionUserCycleDomain {

    // relation table: begin
    private SessionVO session;

    private UserVO user;
    // relation table: end
}
