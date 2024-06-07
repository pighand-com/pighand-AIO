package com.pighand.aio.vo.base;

import com.pighand.aio.domain.base.UserDomain;
import com.pighand.aio.domain.base.UserExtensionDomain;
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
