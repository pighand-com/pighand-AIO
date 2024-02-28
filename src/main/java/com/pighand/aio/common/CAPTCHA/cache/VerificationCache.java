package com.pighand.aio.common.CAPTCHA.cache;

import com.pighand.aio.common.CAPTCHA.CodeData;
import com.pighand.aio.common.CAPTCHA.image.Flicker;
import com.pighand.aio.common.enums.CacheConfigEnum;
import lombok.AllArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * 验证码缓存
 */
@Component
@AllArgsConstructor
public class VerificationCache {

    private final CacheManager cacheManager;

    /**
     * 缓存key
     *
     * @param projectId
     * @param uuid
     * @return
     */
    private String cacheKey(String projectId, String uuid) {
        return "CAPTCHA_" + projectId + "_" + uuid;
    }

    /**
     * 获取缓存
     *
     * @param projectId
     * @param uuid
     * @return
     */
    public String getCode(String projectId, String uuid) {
        Cache cache = cacheManager.getCache(CacheConfigEnum.CAPTCHA_CACHE);

        Cache.ValueWrapper valueWrapper = cache.get(this.cacheKey(projectId, uuid));

        String code = Optional.ofNullable(valueWrapper).map(Cache.ValueWrapper::get).map(Object::toString).orElse(null);

        return code;
    }

    /**
     * 获取新验证码
     *
     * @param projectId
     * @return
     */
    public CodeData getNewCode(String projectId) {
        CodeData codeData = new Flicker().getCodeData();

        String uuid = UUID.randomUUID().toString();
        codeData.setCaptchaId(uuid);

        this.setCode(projectId, uuid, codeData.getCode());

        return codeData;
    }

    /**
     * 是否需要验证
     *
     * @param projectId
     * @param uuid
     * @return
     */
    public boolean needVerification(String projectId, String uuid) {
        String code = this.getCode(projectId, uuid);
        return code != null;
    }

    /**
     * 设置验证码
     *
     * @param projectId
     * @param uuid
     * @param code
     */
    public void setCode(String projectId, String uuid, String code) {
        String key = this.cacheKey(projectId, uuid);

        Cache cacheRequire = cacheManager.getCache(CacheConfigEnum.CAPTCHA_REQUIRE_CACHE);
        cacheRequire.put(key, true);

        Cache cacheCode = cacheManager.getCache(CacheConfigEnum.CAPTCHA_CACHE);
        cacheCode.put(key, code);
    }

    /**
     * 清除缓存
     *
     * @param projectId
     * @param ip
     */
    public void clear(String projectId, String uuid) {
        String key = this.cacheKey(projectId, uuid);

        cacheManager.getCache(CacheConfigEnum.CAPTCHA_REQUIRE_CACHE).evict(key);
        cacheManager.getCache(CacheConfigEnum.CAPTCHA_CACHE).evict(key);
    }
}
