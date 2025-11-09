package com.pighand.aio.common.CAPTCHA.cache;

import com.pighand.aio.common.CAPTCHA.CodeData;
import com.pighand.aio.common.CAPTCHA.image.Static;
import com.pighand.aio.common.enums.CacheConfigEnum;
import com.pighand.framework.spring.util.VerifyUtils;
import lombok.AllArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

/**
 * 验证码缓存
 * <p>
 * CAPTCHA_REQUIRE_CACHE 是否需要校验。保存内容：{cacheKey: 验证码id}
 * CAPTCHA_CACHE    验证码内容：{验证码id: code}
 * <p>
 * ModeEnum.ARRAY 模式下，始终需要验证码，CAPTCHA_REQUIRE_CACHE为空。根据验证码id，从CAPTCHA_CACHE取值，判断code是否正确
 * ModeEnum.RETRY 模式下，第一次错误后需要验证码，CAPTCHA_REQUIRE_CACHE、CAPTCHA_CACHE都存在值。根据cacheKey获取验证码id，对比验证码id和code
 * <p>
 */
@Component
@AllArgsConstructor
public class VerificationCache {

    private final CacheManager cacheManager;

    /**
     * 缓存key
     *
     * @param applicationId
     * @param keyValue      缓存的关键字值，如用户名、邮箱等
     * @return
     */
    public String cacheKey(String applicationId, String keyValue) {
        if (VerifyUtils.isEmpty(keyValue)) {
            return null;
        }

        return "CAPTCHA_" + applicationId + "_" + keyValue;
    }

    /**
     * 获取验证码id
     *
     * @param cacheKey
     * @return
     */
    public String getCaptchaId(String cacheKey) {
        Cache cache = cacheManager.getCache(CacheConfigEnum.CAPTCHA_REQUIRE_CACHE);
        Cache.ValueWrapper valueWrapper = cache.get(cacheKey);

        return Optional.ofNullable(valueWrapper).map(Cache.ValueWrapper::get).map(Object::toString).orElse(null);
    }

    /**
     * 获取缓存
     *
     * @param captchaId
     * @return
     */
    public String getCode(String captchaId) {
        Cache cache = cacheManager.getCache(CacheConfigEnum.CAPTCHA_CACHE);

        Cache.ValueWrapper valueWrapper = cache.get(captchaId);

        return Optional.ofNullable(valueWrapper).map(Cache.ValueWrapper::get).map(Object::toString).orElse(null);
    }

    /**
     * 获取新验证码
     *
     * @return
     */
    public CodeData getNewCode() {
        return getNewCode(null);
    }

    /**
     * 获取新验证码
     *
     * @param cacheKey
     * @return
     */
    public CodeData getNewCode(String cacheKey) {
        // TODO 支持配置
        //        CodeData codeData = new Flicker().getCodeData();
        CodeData codeData = new Static().getCodeData();

        String uuid = UUID.randomUUID().toString();
        codeData.setCaptchaId(uuid);

        this.setCode(cacheKey, codeData.getCaptchaId(), codeData.getCode());

        codeData.setCode(null);
        return codeData;
    }

    /**
     * 设置验证码
     *
     * @param cacheKey
     * @param captchaId
     * @param code
     */
    public void setCode(String cacheKey, String captchaId, String code) {
        if (VerifyUtils.isNotEmpty(cacheKey)) {
            Cache cacheRequire = cacheManager.getCache(CacheConfigEnum.CAPTCHA_REQUIRE_CACHE);
            cacheRequire.put(cacheKey, captchaId);
        }

        Cache cacheCode = cacheManager.getCache(CacheConfigEnum.CAPTCHA_CACHE);
        cacheCode.put(captchaId, code);
    }

    /**
     * 清除缓存
     *
     * @param cacheKey
     */
    public void clear(String cacheKey) {
        String captchaId = getCaptchaId(cacheKey);

        cacheManager.getCache(CacheConfigEnum.CAPTCHA_REQUIRE_CACHE).evict(cacheKey);
        cacheManager.getCache(CacheConfigEnum.CAPTCHA_CACHE).evict(captchaId);
    }
}
