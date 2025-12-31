package com.pighand.aio.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import com.pighand.framework.spring.base.BaseEnum;
import lombok.Getter;

/**
 * 用户 - 抖音 - 来源平台
 */
@Getter
public enum DouyinSourcePlatformEnum implements BaseEnum {
    /**
     * 抖音小程序
     */
    MINI_PROGRAM(32),

    /**
     * 抖音小游戏
     */
    MINI_GAME(33);

    @JsonValue
    @EnumValue
    private final Integer value;

    DouyinSourcePlatformEnum(int value) {
        this.value = value;
    }
}
