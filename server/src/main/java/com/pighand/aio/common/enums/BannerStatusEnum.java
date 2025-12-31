package com.pighand.aio.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import com.pighand.framework.spring.base.BaseEnum;
import lombok.Getter;

/**
 * CMS - Banner - 状态
 */
@Getter
public enum BannerStatusEnum implements BaseEnum {
    /**
     * 下架
     */
    UNLISTED(0),

    /**
     * 上架
     */
    LISTED(10);

    @JsonValue
    @EnumValue
    private final Integer value;

    BannerStatusEnum(int value) {
        this.value = value;
    }
}
