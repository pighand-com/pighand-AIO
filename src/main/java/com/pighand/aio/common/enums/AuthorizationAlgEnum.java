package com.pighand.aio.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;

/**
 * 授权算法枚举
 *
 * @author wangshuli
 */
public enum AuthorizationAlgEnum {

    HMAC256("HMAC256"),

    HMAC384("HMAC384"),

    HMAC512("HMAC512"),

    HS256("HS256");

    @JsonValue
    @EnumValue
    public final String value;

    AuthorizationAlgEnum(String value) {
        this.value = value;
    }
}
