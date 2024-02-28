package com.pighand.aio.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum CacheConfigEnum {
    // 其他平台client缓存
    CLIENT(CacheConfigEnum.CLIENT_CACHE, 10, 60 * 60),

    // 验证码缓存是否需要验证
    CAPTCHA_REQUIRE(CacheConfigEnum.CAPTCHA_REQUIRE_CACHE, null, 60 * 30),

    // 验证码缓存
    CAPTCHA(CacheConfigEnum.CAPTCHA_CACHE, null, 60 * 5);

    public static final String CLIENT_CACHE = "client";

    public static final String CAPTCHA_REQUIRE_CACHE = "CAPTCHA_REQUIRE";

    public static final String CAPTCHA_CACHE = "CAPTCHA";

    @Getter
    private String name;

    /**
     * 最大数量
     */
    @Getter
    private Integer maxSize;

    /**
     * 过期时间 秒
     */
    @Getter
    private Integer ttl;

}
