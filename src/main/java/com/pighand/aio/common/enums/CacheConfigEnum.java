package com.pighand.aio.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum CacheConfigEnum {
    CLIENT(CacheConfigEnum.CLIENT_CACHE_NAME, 10, 60 * 60),

    CAPTCHA(CacheConfigEnum.CAPTCHA_CACHE_NAME, null, 60 * 5);

    public static final String CLIENT_CACHE_NAME = "client";

    public static final String CAPTCHA_CACHE_NAME = "CAPTCHA";

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
