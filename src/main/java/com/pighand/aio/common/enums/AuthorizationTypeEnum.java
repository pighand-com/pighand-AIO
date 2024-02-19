package com.pighand.aio.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;

/**
 * 授权类型
 *
 * @author wangshuli
 */
public enum AuthorizationTypeEnum {
    /**
     * jwt
     */
    JWT(1),

    /**
     * hash
     */
    HASH(2);

    @JsonValue
    @EnumValue
    public final int value;

    AuthorizationTypeEnum(int value) {
        this.value = value;
    }
}
