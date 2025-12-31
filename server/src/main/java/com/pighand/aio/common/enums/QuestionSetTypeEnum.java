package com.pighand.aio.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import com.pighand.framework.spring.base.BaseEnum;
import lombok.Getter;

/**
 * CMS - 题目 - 类型
 */
@Getter
public enum QuestionSetTypeEnum implements BaseEnum {
    /**
     * 文本回答
     */
    TEXT(10),

    /**
     * 单选
     */
    SINGLE_CHOICE(20),

    /**
     * 多选
     */
    MULTIPLE_CHOICE(21);

    @JsonValue
    @EnumValue
    private final Integer value;

    QuestionSetTypeEnum(int value) {
        this.value = value;
    }
}
