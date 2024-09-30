package com.pighand.aio.common.sdk.tencentCloud;

import com.pighand.aio.common.enums.CacheConfigEnum;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.http.HttpProtocol;
import com.qcloud.cos.region.Region;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class Client {

    @Cacheable(cacheNames = {CacheConfigEnum.CLIENT_CACHE}, key = "'cos_' + #region + '_' + #appid")
    public COSClient cosClient(String appid, String secret, String region) {
        COSCredentials cred = new BasicCOSCredentials(appid, secret);

        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setRegion(new Region(region));
        clientConfig.setHttpProtocol(HttpProtocol.https);

        return new COSClient(cred, clientConfig);
    }
}
