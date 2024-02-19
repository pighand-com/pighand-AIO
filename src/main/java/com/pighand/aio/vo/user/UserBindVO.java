package com.pighand.aio.vo.user;

import com.mybatisflex.annotation.Column;
import com.pighand.aio.domain.user.UserBindDomain;
import lombok.Data;

/**
 * 用户 - 绑定信息
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Data
public class UserBindVO extends UserBindDomain {
    @Column(ignore = true)
    private UserVO user;
}
