package com.pighand.aio.common.interceptor;

import com.pighand.aio.vo.base.LoginUser;

/**
 * 拦截器上下文
 */
public class Context {
    /**
     * 获取应用id
     *
     * @return
     */
    public static Long getApplicationId() {
        return AuthorizationInterceptor.getApplicationId();
    }

    /**
     * 获取登录用户
     *
     * @return null 未登录
     */
    public static LoginUser getLoginUser() {
        return AuthorizationInterceptor.getLoginUser();
    }
}
