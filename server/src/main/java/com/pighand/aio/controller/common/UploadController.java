package com.pighand.aio.controller.common;

import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.common.interfaces.Authorization;
import com.pighand.aio.common.sdk.tencentCloud.COSSDK;
import com.pighand.aio.vo.base.LoginUser;
import com.pighand.framework.spring.api.annotation.Get;
import com.pighand.framework.spring.api.annotation.Post;
import com.pighand.framework.spring.api.annotation.RestController;
import com.pighand.framework.spring.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

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

    @Value("${upload.path}")
    private String uploadPath;
    @Value("${upload.web-path}")
    private String webPath;
    @Value("${server.domain}")
    private String domain;

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

    @Post(path = "server", docSummary = "上传至服务器")
    public Result uploadServer(@RequestParam("files") MultipartFile[] files) {
        LoginUser loginUserInfo = Context.getLoginUser();

        File directory = new File(uploadPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        List<String> urls = new ArrayList<>(files.length);
        for (MultipartFile file : files) {
            String fileName =
                loginUserInfo.getId() + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename()
                    .replaceAll(" ", "");

            // TODO: 使用URI拼接url
            //            URI uri = null;
            //            try {
            //                uri = new URI(domain).resolve(webPath).resolve(fileName);
            //            } catch (URISyntaxException e) {
            //                return new Result().exception("上传文件失败：" + e.getMessage());
            //            }
            //            urls.add(uri.toString());
            urls.add(domain + "/" + webPath + "/" + fileName);

            try {
                File uploadedFile = new File(directory, fileName);
                file.transferTo(uploadedFile);
            } catch (IOException e) {
                return new Result().exception("上传文件失败：" + e.getMessage());
            }
        }

        return new Result(urls);

    }

}
