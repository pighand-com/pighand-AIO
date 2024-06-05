package com.pighand.aio.vo.user;

import com.pighand.aio.domain.user.UserDouyinDomain;
import lombok.Data;

/**
 * 用户 - 抖音
 *
 * @author wangshuli
 * @createDate 2024-06-05 23:58:27
 */
@Data
public class UserDouyinVO extends UserDouyinDomain {

    // relation table: begin
    private UserVO user;
    // relation table: end
}
