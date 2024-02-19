package com.pighand.aio.common.CAPTCHA;

import com.pighand.aio.common.CAPTCHA.cache.VerificationCache;
import com.pighand.framework.spring.exception.ThrowPrompt;
import com.pighand.framework.spring.util.VerifyUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

/**
 * 验证码拦截器
 * <p>
 */
@Component
@AllArgsConstructor
public class CAPTCHAInterceptor implements HandlerInterceptor {

    private final VerificationCache verificationCache;

    /**
     * 校验验证码
     *
     * @param mode
     * @param projectId
     * @param ip
     * @param code
     */
    private void checkCode(ModeEnum mode, String projectId, String ip, String code) {
        if (mode == null) {
            return;
        }

        if (mode.equals(ModeEnum.RETRY)) {
            boolean needVerification = verificationCache.needVerification(projectId, ip);

            if (VerifyUtils.isNotEmpty(code) && !needVerification) {
                return;
            }
        }

        CodeData codedata = verificationCache.getCode(projectId, ip);

        if (VerifyUtils.isEmpty(code)) {
            throw new ThrowPrompt("请填写验证码", 40310, codedata.getByteArray());
        }

        if (!codedata.getCode().equals(code)) {
            CodeData newCode = verificationCache.newCode(projectId, ip);
            throw new ThrowPrompt("验证码错误", 40311, newCode.getByteArray());
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (handler instanceof HandlerMethod handlerMethod) {
            CAPTCHA methodAnnotation = handlerMethod.getMethodAnnotation(CAPTCHA.class);
            CAPTCHA classAnnotation = handlerMethod.getBeanType().getAnnotation(CAPTCHA.class);

            ModeEnum mode = Optional.ofNullable(methodAnnotation).map(CAPTCHA::value)
                .orElse(Optional.ofNullable(classAnnotation).map(CAPTCHA::value).orElse(null));

            this.checkCode(mode, request.getHeader("projectId"), request.getHeader("ip"), request.getHeader("code"));
        }

        return true;
    }
}
