package com.pighand.aio.controller.common;

import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.common.interfaces.Authorization;
import com.pighand.aio.common.sdk.tencentCloud.COSSDK;
import com.pighand.aio.vo.user.LoginUser;
import com.pighand.framework.spring.api.annotation.Get;
import com.pighand.framework.spring.api.annotation.RestController;
import com.pighand.framework.spring.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URL;

/**
 * 上传接口
 *
 * @author wangshuli
 * @createDate 2023-05-13 11:49:52
 */
@Authorization()
@RequiredArgsConstructor
@RestController(path = "upload", docName = "上传接口")
public class UploadController {

    private final COSSDK service;

    /**
     * @param extension 扩展名
     * @return
     */
    @Get(path = "url", docSummary = "获取上传授权rul")
    public Result getUploadUrl(@RequestParam(required = false) String extension,
        @RequestParam(required = false) String path) {
        LoginUser loginUserInfo = Context.getLoginUser();

        URL url = service.generatePresignedUrl(loginUserInfo.getId(), extension, path);

        return new Result(url.toString());
    }

}
