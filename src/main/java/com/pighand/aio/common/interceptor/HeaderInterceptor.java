package com.pighand.aio.common.interceptor;

import io.netty.util.concurrent.FastThreadLocal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * header拦截器
 * <p> Project-Id 必填
 */
public class HeaderInterceptor implements HandlerInterceptor {
    private static final FastThreadLocal<Long> projectIdLocal = new FastThreadLocal<>();

    public static Long getProjectId() {
        return projectIdLocal.get();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String projectId = request.getHeader("Project-Id");
        if (projectId == null || projectId.isEmpty()) {
            //            throw new ThrowPrompt("Project-Id header is missing");
            projectId = "1";
        }

        projectIdLocal.set(Long.parseLong(projectId));
        return true;
    }
}
