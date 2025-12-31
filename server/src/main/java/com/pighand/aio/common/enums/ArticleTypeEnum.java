package com.pighand.aio.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import com.pighand.framework.spring.base.BaseEnum;
import lombok.Getter;

/**
 * CMS - 文章 - 类型
 */
@Getter
public enum ArticleTypeEnum implements BaseEnum {
    /**
     * 图片
     */
    IMAGE(10),

    /**
     * 视频
     */
    VIDEO(20),

    /**
     * 文件
     */
    FILE(30);

    @JsonValue
    @EnumValue
    private final Integer value;

    ArticleTypeEnum(int value) {
        this.value = value;
    }
}
