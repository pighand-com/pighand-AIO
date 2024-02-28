package com.pighand.aio.controller.common;

import com.pighand.aio.common.CAPTCHA.CodeData;
import com.pighand.aio.common.CAPTCHA.cache.VerificationCache;
import com.pighand.aio.common.interceptor.Context;
import com.pighand.framework.spring.api.annotation.Get;
import com.pighand.framework.spring.api.annotation.RestController;
import com.pighand.framework.spring.response.Result;
import lombok.RequiredArgsConstructor;

/**
 * 人机验证
 */
@RequiredArgsConstructor
@RestController(path = "CAPTCHA", docName = "人机验证")
public class CAPTCHAController {

    private final VerificationCache verificationCache;

    @Get(path = "code", docSummary = "获取验证码")
    public Result<CodeData> getCode() {
        Long projectId = Context.getProjectId();
        CodeData codeData = verificationCache.getNewCode(projectId.toString());

        return new Result(codeData);
    }
}
