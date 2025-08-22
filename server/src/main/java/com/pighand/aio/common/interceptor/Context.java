package com.pighand.aio.common.interceptor;

import com.pighand.aio.vo.base.LoginUser;

import java.util.Date;

/**
 * 拦截器上下文
 */
public class Context {
    /**
     * 获取应用id
     *
     * @return
     */
    public static Long applicationId() {
        return AuthorizationInterceptor.applicationIdLocal();
    }

    /**
     * 获取店铺id
     *
     * @return
     */
    public static Long storeId() {
        return AuthorizationInterceptor.storeIdLocal();
    }

    /**
     * 获取登录用户
     *
     * @return null 未登录
     */
    public static LoginUser loginUser() {
        return AuthorizationInterceptor.authorizationInfoLocal();
    }

    /**
     * 获取当前时间
     *
     * @return
     */
    public static Date now() {
        return AuthorizationInterceptor.nowLocal();
    }
}
