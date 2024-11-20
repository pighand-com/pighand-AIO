package com.pighand.aio.common.interceptor;

import com.pighand.aio.common.interfaces.ApplicationId;
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

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.function.Function;

/**
 * 登录拦截器
 * <p>
 */
@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    public static final String HEADER_APPLICATION_ID = "X-Application-Id";
    public static final String HEADER_AUTHORIZATION = "Authorization";

    private static final FastThreadLocal<LoginUser> currentUser = new FastThreadLocal<>();

    private static final FastThreadLocal<Long> applicationIdLocal = new FastThreadLocal<>();

    @Resource
    private AuthorizationService authorizationService;

    public static LoginUser getLoginUser() {
        return currentUser.get();
    }

    public static Long getApplicationId() {
        return applicationIdLocal.get();
    }

    private boolean isAnnotationRequired(HandlerMethod handlerMethod, Class<? extends Annotation> annotationClass,
        Function<Annotation, Boolean> valueExtractor) {
        Annotation methodAnnotation = handlerMethod.getMethodAnnotation(annotationClass);
        Annotation classAnnotation = handlerMethod.getBeanType().getAnnotation(annotationClass);

        if (methodAnnotation != null || classAnnotation != null) {
            return Optional.ofNullable(methodAnnotation).map(valueExtractor)
                .orElse(Optional.ofNullable(classAnnotation).map(valueExtractor).orElse(false));
        }
        return false;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 先清空，防止线程池复用，导致使用上一个线程的登录信息
        currentUser.remove();

        // 判断ApplicationId是否必须
        boolean isApplicationRequired = false;
        // 判断是否必须登录
        boolean isLoginRequired = false;
        if (handler instanceof HandlerMethod handlerMethod) {
            isLoginRequired = isAnnotationRequired(handlerMethod, Authorization.class,
                annotation -> ((Authorization)annotation).value());

            isApplicationRequired = isAnnotationRequired(handlerMethod, ApplicationId.class,
                annotation -> ((ApplicationId)annotation).value());
        }

        String authorization = request.getHeader(HEADER_AUTHORIZATION);
        if (isLoginRequired && VerifyUtils.isEmpty(authorization)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        // 解析ApplicationId
        String applicationId = request.getHeader(HEADER_APPLICATION_ID);
        if (VerifyUtils.isEmpty(applicationId)) {
            applicationIdLocal.set(Long.parseLong(applicationId));
        } else if (isApplicationRequired) {
            try {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, HEADER_APPLICATION_ID + " is required");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
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
