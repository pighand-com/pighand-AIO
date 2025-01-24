package com.pighand.aio.service.common.impl;

import com.pighand.aio.common.enums.PlatformEnum;
import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.common.sdk.tencentCloud.TencentCloudSDK;
import com.pighand.aio.domain.base.ApplicationPlatformKeyDomain;
import com.pighand.aio.service.base.ApplicationPlatformKeyService;
import com.pighand.aio.service.common.UploadService;
import com.pighand.aio.vo.base.LoginUser;
import com.pighand.aio.vo.other.UploadRequestVO;
import com.pighand.aio.vo.other.UploadResponseVO;
import com.pighand.aio.vo.other.UploadUrlVO;
import com.pighand.framework.spring.exception.ThrowException;
import com.pighand.framework.spring.exception.ThrowPrompt;
import com.pighand.framework.spring.util.VerifyUtils;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.DeleteObjectTaggingRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UploadServiceImpl implements UploadService {

    private final ApplicationPlatformKeyService projectPlatformKeyService;
    private final TencentCloudSDK tencentCloudSDK;

    // COS临时文件TAG
    private final String COS_TMP_FILE_KEY = "x-cos-tagging";
    private final String COS_TMP_FILE_VALUE = "tmp=true";

    // 过期时间
    private final Integer expirationTime = 1000 * 60 * 3;

    @Value("${upload.path}")
    private String uploadPath;
    @Value("${upload.web-path}")
    private String webPath;
    @Value("${server.domain}")
    private String domain;

    /**
     * 文件名：{path}/用户ID_时间戳_序号_线程id.扩展名
     *
     * @param extension
     * @param path
     * @param index     序号，默认3位随机数，防止批量上传可能存在同名问题
     * @return
     */
    @Override
    public String filePath(String extension, String path, Integer index) {
        if (index == null) {
            index = Integer.valueOf(String.format("%03d", new Random().nextInt(10000)));
        }

        String key =
            Context.loginUser().getId() + "_" + System.currentTimeMillis() + "_" + index + "_" + Thread.currentThread()
                .getId();
        if (VerifyUtils.isNotEmpty(extension)) {
            if (extension.startsWith(".")) {
                key += extension;
            } else {
                key += "." + extension;
            }
        }

        if (VerifyUtils.isNotEmpty(path)) {
            key = path + "/" + key;
        }

        return key;
    }

    /*
     * 根据url获取桶中的文件key
     */
    @Override
    public String fileKeyByUrl(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            return url.getPath().substring(1);
        } catch (MalformedURLException e) {
            throw new ThrowException("Invalid URL: " + fileUrl, e);
        }
    }

    @Override
    public UploadResponseVO byAuthUrl(List<UploadRequestVO> uploads) {
        ApplicationPlatformKeyDomain projectPlatformKey = projectPlatformKeyService.uploadKey();

        if (projectPlatformKey.getPlatform().equals(PlatformEnum.SYSTEM)) {
            throw new ThrowPrompt("仅支持上传到服务器");
        }

        Date expirationDate = new Date(System.currentTimeMillis() + expirationTime);
        List<UploadUrlVO> urls = new ArrayList<>(uploads.size());

        HashMap headers = new HashMap<>(1);

        COSClient cosClient = tencentCloudSDK.cosClient(projectPlatformKey.getAppid(), projectPlatformKey.getSecret(),
            projectPlatformKey.getRegion());

        switch (projectPlatformKey.getPlatform()) {
            case TENCENT_CLOUD_COS:
                headers.put(COS_TMP_FILE_KEY, COS_TMP_FILE_VALUE);

                for (int i = 0; i < uploads.size(); i++) {
                    UploadRequestVO upload = uploads.get(i);

                    String key = filePath(upload.getExtension(), upload.getPath(), i);
                    String uploadUrl =
                        cosClient.generatePresignedUrl(projectPlatformKey.getBucket(), key, expirationDate,
                            HttpMethodName.PUT, headers, new HashMap<>(0)).toString();

                    UploadUrlVO uploadUrlVO = new UploadUrlVO();
                    uploadUrlVO.setUploadUrl(uploadUrl);
                    uploadUrlVO.setUrl(uploadUrl.split("\\?")[0]);
                    urls.add(uploadUrlVO);
                }
                break;
            default:
                throw new ThrowPrompt("不支持上传类型");
        }

        UploadResponseVO uploadResponse = new UploadResponseVO();
        uploadResponse.setUrls(urls);
        uploadResponse.setHeaders(headers);

        return uploadResponse;
    }

    /**
     * 将临时文件改为正式文件
     *
     * @param fileUrl
     * @return
     */
    @Override
    public void updateFileOfficial(String fileUrl) {
        ApplicationPlatformKeyDomain projectPlatformKey = projectPlatformKeyService.uploadKey();

        String key = fileKeyByUrl(fileUrl);

        COSClient cosClient = tencentCloudSDK.cosClient(projectPlatformKey.getAppid(), projectPlatformKey.getSecret(),
            projectPlatformKey.getRegion());

        switch (projectPlatformKey.getPlatform()) {
            case TENCENT_CLOUD_COS:
                cosClient.deleteObjectTagging(new DeleteObjectTaggingRequest(projectPlatformKey.getBucket(), key));
                break;
            default:
                throw new ThrowPrompt("不支持上传类型");
        }

    }

    @Override
    public List<String> byLocal(MultipartFile[] files) {
        ApplicationPlatformKeyDomain projectPlatformKey = projectPlatformKeyService.uploadKey();

        if (!projectPlatformKey.getPlatform().equals(PlatformEnum.SYSTEM)) {
            throw new ThrowPrompt("不支持上传到服务器");
        }

        LoginUser loginUserInfo = Context.loginUser();

        File directory = new File(uploadPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        List<String> urls = new ArrayList<>(files.length);
        for (MultipartFile file : files) {
            String fileName =
                loginUserInfo.getId() + "_" + System.currentTimeMillis() + "_" + file.getOriginalFilename()
                    .replaceAll(" ", "");

            URI uri = null;
            try {
                uri = new URI(domain).resolve(webPath).resolve(fileName);
            } catch (URISyntaxException e) {
                throw new ThrowException("上传文件失败：" + e.getMessage());
            }
            urls.add(uri.toString());

            try {
                File uploadedFile = new File(directory, fileName);
                file.transferTo(uploadedFile);
            } catch (IOException e) {
                throw new ThrowException("上传文件失败：" + e.getMessage());
            }
        }

        return urls;
    }
}
