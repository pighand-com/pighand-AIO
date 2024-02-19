package com.pighand.aio.vo.user;

import com.pighand.aio.domain.user.UserDomain;
import com.pighand.aio.domain.user.UserExtensionDomain;
import lombok.Data;

/**
 * 用户关键信息表，主要保存登录所用信息
 *
 * @author wangshuli
 * @createDate 2023-03-25 18:45:58
 */
@Data
public class UserVO extends UserDomain {
    private UserExtensionDomain extension;

    private String token;

    private Integer bindCount;
}
