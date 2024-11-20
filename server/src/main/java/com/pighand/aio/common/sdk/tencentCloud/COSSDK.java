package com.pighand.aio.common.sdk.tencentCloud;

import com.pighand.aio.common.enums.PlatformEnum;
import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.domain.base.ApplicationPlatformKeyDomain;
import com.pighand.aio.service.base.ApplicationPlatformKeyService;
import com.pighand.framework.spring.util.VerifyUtils;
import com.qcloud.cos.http.HttpMethodName;
import com.qcloud.cos.model.ObjectMetadata;
import com.qcloud.cos.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 腾讯云COS服务
 *
 * @author wangshuli
 */
@RequiredArgsConstructor
@Service
public class COSSDK {

    private final Client client;

    private final ApplicationPlatformKeyService projectPlatformKeyService;

    /**
     * 获取COS临时上传URL
     *
     * @param loginUserId
     * @param extension
     * @param path
     * @return
     */
    public URL generatePresignedUrl(Long loginUserId, String extension, String path) {
        ApplicationPlatformKeyDomain projectPlatformKeyDomain =
            projectPlatformKeyService.findByPlatform(Context.getProjectId(), PlatformEnum.TENCENT_CLOUD_COS);

        String key = loginUserId + "_" + System.currentTimeMillis();
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

        HttpMethodName method = HttpMethodName.PUT;

        // 授权URL过期时间
        Date expirationDate = new Date(System.currentTimeMillis() + 30 * 60 * 1000);

        Map<String, String> params = new HashMap<String, String>();

        Map<String, String> headers = new HashMap<String, String>();

        return client.cosClient(projectPlatformKeyDomain.getAppid(), projectPlatformKeyDomain.getSecret(),
                projectPlatformKeyDomain.getRegion())
            .generatePresignedUrl(projectPlatformKeyDomain.getBucket(), key, expirationDate, method, headers, params);
    }

    /**
     * 简单上传
     *
     * @param key
     * @param inputStream
     */
    public String upload(String key, InputStream inputStream, Integer size) {
        ApplicationPlatformKeyDomain projectPlatformKeyDomain =
            projectPlatformKeyService.findByPlatform(Context.getProjectId(), PlatformEnum.TENCENT_CLOUD_COS);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(size);

        PutObjectRequest putObjectRequest =
            new PutObjectRequest(projectPlatformKeyDomain.getBucket(), key, inputStream, objectMetadata);
        client.cosClient(projectPlatformKeyDomain.getAppid(), projectPlatformKeyDomain.getSecret(),
            projectPlatformKeyDomain.getRegion()).putObject(putObjectRequest);

        return "https://" + projectPlatformKeyDomain.getBucket() + ".cos." + projectPlatformKeyDomain.getRegion()
            + ".myqcloud.com/" + key;
    }
}
