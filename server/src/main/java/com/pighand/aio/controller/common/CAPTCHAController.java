package com.pighand.aio.controller.common;

import com.pighand.aio.common.CAPTCHA.CodeData;
import com.pighand.aio.common.CAPTCHA.cache.VerificationCache;
import com.pighand.aio.common.interceptor.Context;
import com.pighand.framework.spring.api.annotation.Get;
import com.pighand.framework.spring.api.annotation.RestController;
import com.pighand.framework.spring.response.Result;
import com.pighand.framework.spring.util.VerifyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 人机验证
 */
@RequiredArgsConstructor
@RestController(path = {"client/CAPTCHA", "dashboard/CAPTCHA"}, docName = "人机验证")
public class CAPTCHAController {

    private final VerificationCache verificationCache;

    @Get(path = "code", docSummary = "获取验证码")
    public Result<CodeData> getCode(@RequestParam(required = true) String key) {
        String applicationIdString = "_";

        Long applicationId = Context.applicationId();
        if (VerifyUtils.isNotEmpty(applicationId)) {
            applicationIdString = applicationId.toString();
        }

        String cacheKey = verificationCache.cacheKey(applicationIdString, key);
        CodeData codeData = verificationCache.getNewCode(cacheKey);

        return new Result(codeData);
    }
}
