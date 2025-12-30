package com.pighand.aio.common.interceptor;

import com.pighand.aio.common.interfaces.ApplicationId;
import com.pighand.aio.common.interfaces.Authorization;
import com.pighand.aio.service.base.AuthorizationService;
import com.pighand.aio.vo.base.LoginUser;
import com.pighand.framework.spring.interceptor.RequestInterceptor;
import com.pighand.framework.spring.util.VerifyUtils;
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

    public static final String HEADER_STORE_ID = "X-Store-Id";

    private static final ScopedValue<LoginUser> authorizationInfoLocal = ScopedValue.newInstance();

    private static final ScopedValue<Long> applicationIdLocal = ScopedValue.newInstance();

    private static final ScopedValue<Long> storeIdLocal = ScopedValue.newInstance();

    @Resource
    private AuthorizationService authorizationService;

    public static LoginUser authorizationInfoLocal() {
        return authorizationInfoLocal.get();
    }

    public static Long applicationIdLocal() {
        return applicationIdLocal.get();
    }

    public static Long storeIdLocal() {
        return storeIdLocal.get();
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
            ScopedValue.where(applicationIdLocal, Long.parseLong(applicationId));
        } else if (isApplicationRequired) {
            try {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, HEADER_APPLICATION_ID + " is required");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return false;
        }

        // 解析StoreId
        String storeId = request.getHeader(HEADER_STORE_ID);
        if (VerifyUtils.isNotEmpty(storeId)) {
            ScopedValue.where(storeIdLocal, Long.parseLong(storeId));
        }

        // 解析token
        if (VerifyUtils.isNotEmpty(authorization)) {
            try {
                LoginUser loginUser = authorizationService.checkToken(authorization);

                Long userApplicationId = loginUser.getAId();
                Long userStoreId = loginUser.getSId();

                // token所属应用id与请求头不一致，判断为无效token
                if (isLoginRequired && (
                    (VerifyUtils.isNotEmpty(applicationId) && VerifyUtils.isNotEmpty(userApplicationId)
                        && !userApplicationId.toString().equals(applicationId)) || (VerifyUtils.isNotEmpty(storeId)
                        && VerifyUtils.isNotEmpty(userStoreId) && !userStoreId.toString().equals(storeId)))) {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    return false;
                }

                if (VerifyUtils.isNotEmpty(userApplicationId) && VerifyUtils.isEmpty(applicationId)) {
                    ScopedValue.where(applicationIdLocal, userApplicationId);
                }

                if (VerifyUtils.isNotEmpty(userStoreId) && VerifyUtils.isEmpty(storeId)) {
                    ScopedValue.where(storeIdLocal, userStoreId);
                }

                ScopedValue.where(authorizationInfoLocal, loginUser);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }
        }

        return true;
    }
}
