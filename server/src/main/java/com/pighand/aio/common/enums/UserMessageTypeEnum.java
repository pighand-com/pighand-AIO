package com.pighand.aio.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;

/**
 * 用户消息类型
 *
 * @author wangshuli
 */
public enum UserMessageTypeEnum {
    /**
     * 系统消息
     */
    SYSTEM(10),

    /**
     * 用户消息
     */
    USER(20);

    @JsonValue
    @EnumValue
    public final Integer value;

    UserMessageTypeEnum(Integer value) {
        this.value = value;
    }
}
