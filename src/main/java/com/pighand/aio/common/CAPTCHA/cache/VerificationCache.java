package com.pighand.aio.common.CAPTCHA.cache;

import com.pighand.aio.common.CAPTCHA.CodeData;
import com.pighand.aio.common.CAPTCHA.image.Flicker;
import com.pighand.aio.common.enums.CacheConfigEnum;
import lombok.AllArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class VerificationCache {

    private final CacheManager cacheManager;

    /**
     * 验证码是否需要验证
     *
     * @param projectId
     * @param ip
     * @return
     */
    public boolean needVerification(String projectId, String ip) {
        Cache cache = cacheManager.getCache(CacheConfigEnum.CAPTCHA_CACHE_NAME);
        Cache.ValueWrapper valueWrapper = cache.get("CAPTCHA_" + projectId + "_" + ip);

        return valueWrapper != null;
    }

    /**
     * 获取已发送的验证码，或新验证码
     *
     * @param projectId
     * @param ip
     * @return
     */
    @Cacheable(cacheNames = {CacheConfigEnum.CAPTCHA_CACHE_NAME}, key = "'CAPTCHA_' + #projectId + '_' + #ip")
    public CodeData getCode(String projectId, String ip) {
        return new Flicker().getCodeData();
    }

    /**
     * 获取已发送的验证码，或新验证码
     *
     * @param projectId
     * @param ip
     * @return
     */
    @CachePut(cacheNames = {CacheConfigEnum.CAPTCHA_CACHE_NAME}, key = "'CAPTCHA_' + #projectId + '_' + #ip")
    public CodeData newCode(String projectId, String ip) {
        return new Flicker().getCodeData();
    }
}
