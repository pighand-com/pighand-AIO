package com.pighand.aio.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import com.pighand.framework.spring.base.BaseEnum;
import lombok.Getter;

/**
 * CMS - 素材/文章 - 状态
 */
@Getter
public enum AssetsStatusEnum implements BaseEnum {
    /**
     * 上架
     */
    LISTED(10),

    /**
     * 下架
     */
    UNLISTED(20);

    @JsonValue
    @EnumValue
    private final Integer value;

    AssetsStatusEnum(int value) {
        this.value = value;
    }
}
