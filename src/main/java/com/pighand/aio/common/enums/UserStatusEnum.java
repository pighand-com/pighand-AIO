package com.pighand.aio.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;

/**
 * 用户状态
 *
 * @author wangshuli
 */
public enum UserStatusEnum {
    /**
     * 正常
     */
    NORMAL(10),

    /**
     * 停用
     */
    STOP(20);

    @JsonValue
    @EnumValue
    public final int value;

    UserStatusEnum(int value) {
        this.value = value;
    }
}
