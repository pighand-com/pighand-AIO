package com.pighand.aio.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import com.pighand.framework.spring.base.BaseEnum;
import lombok.Getter;

/**
 * 消息 - 通知模板 - 类型
 */
@Getter
public enum NotifyTemplateTypeEnum implements BaseEnum {
    /**
     * 抽奖
     */
    LOTTERY(100);

    @JsonValue
    @EnumValue
    private final Integer value;

    NotifyTemplateTypeEnum(int value) {
        this.value = value;
    }
}
