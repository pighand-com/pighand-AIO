package com.pighand.aio.controller.common;

import com.pighand.aio.common.interfaces.Authorization;
import com.pighand.aio.service.common.UploadService;
import com.pighand.aio.vo.other.UploadRequestVO;
import com.pighand.aio.vo.other.UploadResponseVO;
import com.pighand.framework.spring.api.annotation.Post;
import com.pighand.framework.spring.api.annotation.RestController;
import com.pighand.framework.spring.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 上传接口
 *
 * @author wangshuli
 * @createDate 2023-05-13 11:49:52
 */
@Authorization()
@RequiredArgsConstructor
@RestController(path = {"client/upload", "dashboard/upload"}, docName = "上传接口")
public class UploadController {

    private final UploadService uploadService;

    @Post(path = "url", docSummary = "获取上传授权rul")
    public Result<UploadResponseVO> getUploadUrl(@RequestBody List<UploadRequestVO> uploads) {
        UploadResponseVO urls = uploadService.byAuthUrl(uploads);

        return new Result(urls);
    }

    @Post(path = "server", docSummary = "上传至服务器")
    public Result uploadServer(@RequestParam("files") MultipartFile[] files) {
        List<String> urls = uploadService.byLocal(files);

        return new Result(urls);

    }

}
