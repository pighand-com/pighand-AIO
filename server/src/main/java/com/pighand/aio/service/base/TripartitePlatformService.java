package com.pighand.aio.service.base;

import com.pighand.aio.vo.base.UserVO;

/**
 * 三方平台方法
 *
 * @author wangshuli
 */
public interface TripartitePlatformService {

    /**
     * 使用code登录
     *
     * @param applicationId 项目id
     * @param code          三方平台code
     * @param anonymousCode 匿名code
     * @return token
     */
    UserVO loginInByCode(Long applicationId, String code, String anonymousCode);
}
