package com.pighand.aio.vo.base;

import com.mybatisflex.annotation.TableRef;
import com.pighand.aio.domain.base.UserDouyinDomain;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用户 - 抖音
 *
 * @author wangshuli
 * @createDate 2024-06-05 23:58:27
 */
@Data
@TableRef(UserDouyinDomain.class)
@EqualsAndHashCode(callSuper = false)
public class UserDouyinVO extends UserDouyinDomain {

    // relation table: begin
    private UserVO user;
    // relation table: end
}
