package com.pighand.aio.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import com.pighand.framework.spring.base.BaseEnum;
import lombok.Getter;

/**
 * 电商 - 场次模板 - 类型
 */
@Getter
public enum SessionTemplateTypeEnum implements BaseEnum {
    /**
     * 按用户分组
     */
    USER_GROUPING(10);

    @JsonValue
    @EnumValue
    private final Integer value;

    SessionTemplateTypeEnum(int value) {
        this.value = value;
    }
}
