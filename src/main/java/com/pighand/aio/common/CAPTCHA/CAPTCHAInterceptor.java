package com.pighand.aio.common.CAPTCHA;

import com.pighand.aio.common.CAPTCHA.cache.VerificationCache;
import com.pighand.framework.spring.exception.ThrowPrompt;
import com.pighand.framework.spring.util.VerifyUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
     * @param mode      重试模式验证码第二次需要验证，其他模式验证码必填
     * @param projectId
     * @param captchaId
     * @param code
     */
    private void checkCode(ModeEnum mode, String projectId, String captchaId, String code) {
        if (mode == null) {
            return;
        }

        // 重试模式，如果不需要验证，直接返回
        if (mode.equals(ModeEnum.RETRY)) {
            boolean needVerification = verificationCache.needVerification(projectId, captchaId);

            if (VerifyUtils.isNotEmpty(code) && !needVerification) {
                return;
            }
        }

        // 其他模式验证码必填
        if (VerifyUtils.isEmpty(code)) {
            CodeData newCode = verificationCache.getNewCode(projectId);
            throw new ThrowPrompt("请填写验证码", 40310, newCode);
        }

        // 对比验证码
        String codeCache = verificationCache.getCode(projectId, captchaId);
        if (VerifyUtils.isEmpty(codeCache) || !codeCache.equals(code)) {
            CodeData newCode = verificationCache.getNewCode(projectId);
            throw new ThrowPrompt("验证码错误", 40311, newCode);
        }

        verificationCache.clear(projectId, captchaId);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
        throws IOException {
        if (handler instanceof HandlerMethod handlerMethod) {
            CAPTCHA methodAnnotation = handlerMethod.getMethodAnnotation(CAPTCHA.class);
            CAPTCHA classAnnotation = handlerMethod.getBeanType().getAnnotation(CAPTCHA.class);

            if (methodAnnotation == null && classAnnotation == null) {
                return true;
            }

            ModeEnum mode = Optional.ofNullable(methodAnnotation).map(CAPTCHA::value)
                .orElse(Optional.ofNullable(classAnnotation).map(CAPTCHA::value).orElse(null));

            String code = "";
            String captchaId = "";
            if (request.getMethod().equals(RequestMethod.GET) || request.getMethod().equals(RequestMethod.DELETE)) {
                code = request.getParameter("code");
                captchaId = request.getParameter("captchaId");
            } else {
                String body = request.getReader().lines().collect(Collectors.joining());

                Matcher matcherCode = Pattern.compile("\"code\":\\s*\"([^\"]+)\"").matcher(body);
                if (matcherCode.find()) {
                    code = matcherCode.group(1);
                }

                Matcher matcherId = Pattern.compile("\"captchaId\":\\s*\"([^\"]+)\"").matcher(body);
                if (matcherId.find()) {
                    captchaId = matcherId.group(1);
                }
            }

            // 通过request获取body或url上的code参数
            this.checkCode(mode, request.getHeader("projectId"), captchaId, code);
        }

        return true;
    }
}
