package com.pighand.aio.common.interceptor;

import com.pighand.aio.common.interfaces.ApplicationId;
import com.pighand.aio.common.interfaces.Authorization;
import com.pighand.aio.service.base.AuthorizationService;
import com.pighand.aio.vo.base.LoginUser;
import com.pighand.framework.spring.interceptor.RequestInterceptor;
import com.pighand.framework.spring.util.VerifyUtils;
import io.netty.util.concurrent.FastThreadLocal;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.function.Function;

/**
 * 授权拦截器
 */
@Component
public class AuthorizationInterceptor extends RequestInterceptor {

    public static final String HEADER_APPLICATION_ID = "X-Application-Id";

    private static final FastThreadLocal<LoginUser> authorizationInfoLocal = new FastThreadLocal<>();

    private static final FastThreadLocal<Long> applicationIdLocal = new FastThreadLocal<>();

    @Resource
    private AuthorizationService authorizationService;

    public static LoginUser authorizationInfoLocal() {
        return authorizationInfoLocal.get();
    }

    public static Long applicationIdLocal() {
        return applicationIdLocal.get();
    }

    @Override
    public Long getAuthorizationId() {
        return Optional.ofNullable(authorizationInfoLocal.get()).map(LoginUser::getId).orElse(null);
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

    public boolean handle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 先清空，防止线程池复用，导致使用上一个线程的登录信息
        authorizationInfoLocal.remove();
        applicationIdLocal.remove();

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
        if (VerifyUtils.isNotEmpty(applicationId)) {
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

                // token所属应用id与请求头不一致，判断为无效token
                if (isLoginRequired && VerifyUtils.isNotEmpty(applicationId) && VerifyUtils.isNotEmpty(
                    loginUser.getAId()) && !loginUser.getAId().toString().equals(applicationId)) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return false;
                }

                authorizationInfoLocal.set(loginUser);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
        }

        return true;
    }
}
