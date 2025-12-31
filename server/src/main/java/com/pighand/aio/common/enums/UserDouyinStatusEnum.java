package com.pighand.aio.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import com.pighand.framework.spring.base.BaseEnum;
import lombok.Getter;

/**
 * 用户 - 抖音 - 状态
 */
@Getter
public enum UserDouyinStatusEnum implements BaseEnum {
    /**
     * 正常
     */
    NORMAL(10),

    /**
     * 停用
     */
    DISABLED(20);

    @JsonValue
    @EnumValue
    private final Integer value;

    UserDouyinStatusEnum(int value) {
        this.value = value;
    }
}
