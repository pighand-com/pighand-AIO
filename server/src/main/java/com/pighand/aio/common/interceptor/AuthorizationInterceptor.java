package com.pighand.aio.common.interceptor;

import com.pighand.aio.common.interfaces.Authorization;
import com.pighand.aio.service.base.AuthorizationService;
import com.pighand.aio.vo.base.LoginUser;
import com.pighand.framework.spring.util.VerifyUtils;
import io.netty.util.concurrent.FastThreadLocal;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

/**
 * 登录拦截器
 * <p>
 */
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    private static final FastThreadLocal<LoginUser> currentUser = new FastThreadLocal<>();

    @Resource
    private AuthorizationService authorizationService;

    public static LoginUser getLoginUser() {
        return currentUser.get();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 先清空，防止线程池复用，导致使用上一个线程的登录信息
        currentUser.remove();

        // 判断是会否必须登录
        boolean isLoginRequired = false;
        if (handler instanceof HandlerMethod handlerMethod) {
            Authorization methodLogin = handlerMethod.getMethodAnnotation(Authorization.class);
            Authorization classLogin = handlerMethod.getBeanType().getAnnotation(Authorization.class);

            isLoginRequired = methodLogin != null || classLogin != null;

            if (isLoginRequired) {
                isLoginRequired = Optional.ofNullable(methodLogin).map(Authorization::value)
                    .orElse(Optional.ofNullable(classLogin).map(Authorization::value).orElse(false));
            }
        }

        String authorization = request.getHeader("Authorization");
        if (isLoginRequired && VerifyUtils.isEmpty(authorization)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // 解析token
        if (VerifyUtils.isNotEmpty(authorization)) {
            try {
                LoginUser loginUser = authorizationService.checkToken(authorization);

                currentUser.set(loginUser);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
        }

        return true;
    }
}
