package com.pighand.aio.vo.user;

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
    private UserVO user;

    private Long bindUserId;
}
