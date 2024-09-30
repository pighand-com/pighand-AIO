package com.pighand.aio.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;

/**
 * 默认角色枚举
 */
public enum RoleEnum {
    /**
     * 管理员
     */
    ADMIN(10L),

    /**
     * 运营
     */
    OPERATION(20L),

    /**
     * 用户/客户
     */
    USER(30L);

    @JsonValue
    @EnumValue
    public final Long value;

    RoleEnum(Long value) {
        this.value = value;
    }
}
