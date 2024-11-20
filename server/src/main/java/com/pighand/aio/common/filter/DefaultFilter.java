package com.pighand.aio.common.filter;

import com.pighand.aio.common.CAPTCHA.CAPTCHA;
import com.pighand.aio.common.CAPTCHA.CodeData;
import com.pighand.aio.common.CAPTCHA.ModeEnum;
import com.pighand.aio.common.CAPTCHA.cache.VerificationCache;
import com.pighand.aio.common.interceptor.AuthorizationInterceptor;
import com.pighand.framework.spring.exception.ExceptionHandle;
import com.pighand.framework.spring.exception.ThrowException;
import com.pighand.framework.spring.exception.ThrowPrompt;
import com.pighand.framework.spring.util.VerifyUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 默认拦截器
 */
@Component
public class DefaultFilter extends OncePerRequestFilter {
    private final String EXCEPTION_DATA_FUNCTION_KEY = "CAPTCHA";

    @Resource
    RequestMappingHandlerMapping requestMappingHandlerMapping;
    @Resource
    private VerificationCache verificationCache;

    @Resource
    private ExceptionHandle exceptionHandle;

    /**
     * 注入异常处理方法，用于获取验证码
     */
    public DefaultFilter() {
        ExceptionHandle.PutExceptionDataFunction(EXCEPTION_DATA_FUNCTION_KEY,
            (Object cacheKey) -> verificationCache.getNewCode((String)cacheKey));
    }

    @Override
    public void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
        FilterChain filterChain) throws ServletException, IOException {
        if ("OPTIONS".equals(httpServletRequest.getMethod())) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        HttpServletRequest cacheRequest = this.dispose(httpServletRequest, httpServletResponse);

        if (cacheRequest != null) {
            filterChain.doFilter(cacheRequest, httpServletResponse);
        }
    }

    /**
     * 处理请求
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @return HttpServletRequest 返回null，抛异常，不继续处理；否则返回新的缓存request
     * @throws IOException
     */
    private HttpServletRequest dispose(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
        throws IOException {

        // 校验验证码
        HttpServletRequest request = this.checkCAPTCHA(httpServletRequest, httpServletResponse);

        return request;
    }

    /**
     * 校验验证码
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @return
     * @throws IOException
     */
    private HttpServletRequest checkCAPTCHA(HttpServletRequest httpServletRequest,
        HttpServletResponse httpServletResponse) throws IOException {
        HttpServletRequest finalRequest = httpServletRequest;

        HandlerExecutionChain handlerExecutionChain = null;
        try {
            handlerExecutionChain = requestMappingHandlerMapping.getHandler(httpServletRequest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (handlerExecutionChain == null) {
            return finalRequest;
        }

        Object handler = handlerExecutionChain.getHandler();
        if (!(handler instanceof HandlerMethod handlerMethod)) {
            return finalRequest;
        }

        // 类或方法的CAPTCHA注解
        CAPTCHA methodAnnotation = handlerMethod.getMethodAnnotation(CAPTCHA.class);
        CAPTCHA classAnnotation = handlerMethod.getBeanType().getAnnotation(CAPTCHA.class);

        if (methodAnnotation == null && classAnnotation == null) {
            return finalRequest;
        }

        // 获取CAPTCHA的缓存key
        CAPTCHA finalCAPTCHA = Optional.ofNullable(methodAnnotation).orElse(classAnnotation);
        ModeEnum mode = finalCAPTCHA.value();
        String key = finalCAPTCHA.key();

        if (VerifyUtils.isEmpty(key)) {
            exceptionHandle.setResponse(httpServletRequest, httpServletResponse,
                new ThrowException("验证码key不能为空"));
            return null;
        }

        String captcha = httpServletRequest.getParameter("captcha");
        String captchaId = httpServletRequest.getParameter("captchaId");

        // 获取CAPTCHA的缓存value
        String keyValue = httpServletRequest.getParameter(key);
        if (VerifyUtils.isNotEmpty(key) && VerifyUtils.isEmpty(keyValue)) {
            CachedBodyHttpServletRequest cachedBodyHttpServletRequest =
                new CachedBodyHttpServletRequest(httpServletRequest);
            finalRequest = cachedBodyHttpServletRequest;

            String body = cachedBodyHttpServletRequest.getBody();
            Matcher matcherCode = Pattern.compile("\"" + key + "\":\\s*\"([^\"]+)\"").matcher(body);
            if (matcherCode.find()) {
                keyValue = matcherCode.group(1);
            }
        }

        if (VerifyUtils.isEmpty(keyValue)) {
            exceptionHandle.setResponse(httpServletRequest, httpServletResponse,
                new ThrowException("验证码key对应的值不能为空"));
            return null;
        }
        String applicationId =
            Optional.ofNullable(httpServletRequest.getHeader(AuthorizationInterceptor.HEADER_APPLICATION_ID))
                .orElse("_");
        String cacheKey = verificationCache.cacheKey(applicationId, keyValue);

        if (VerifyUtils.isEmpty(captcha)) {
            CodeData newCode = verificationCache.getNewCode(cacheKey);

            exceptionHandle.setResponse(httpServletRequest, httpServletResponse,
                new ThrowPrompt("请填写验证码", 40310, newCode));
            return null;
        }

        if (VerifyUtils.isEmpty(captchaId)) {
            CodeData newCode = verificationCache.getNewCode(cacheKey);

            exceptionHandle.setResponse(httpServletRequest, httpServletResponse,
                new ThrowPrompt("请填写验证码", 40311, newCode));
            return null;
        }

        // 判断是否需要验证。重试模式，失败一次后需要验证
        String checkCaptchaId = verificationCache.getCaptchaId(cacheKey);
        if (mode.equals(ModeEnum.RETRY) && VerifyUtils.isEmpty(checkCaptchaId)) {
            finalRequest.setAttribute(EXCEPTION_DATA_FUNCTION_KEY, cacheKey);
            return finalRequest;
        }

        // 校验验证码id
        if (VerifyUtils.isEmpty(checkCaptchaId) || !checkCaptchaId.equals(captchaId)) {
            CodeData newCode = verificationCache.getNewCode(cacheKey);

            exceptionHandle.setResponse(httpServletRequest, httpServletResponse,
                new ThrowPrompt("请填写验证码", 40312, newCode));
            return null;
        }

        // 对比验证码
        String codeCache = verificationCache.getCode(captchaId);
        if (VerifyUtils.isEmpty(codeCache) || !codeCache.equalsIgnoreCase(captcha)) {
            CodeData newCode = verificationCache.getNewCode(cacheKey);

            exceptionHandle.setResponse(httpServletRequest, httpServletResponse,
                new ThrowPrompt("验证码错误", 40313, newCode));
            return null;
        }

        verificationCache.clear(cacheKey);

        return finalRequest;
    }

}
