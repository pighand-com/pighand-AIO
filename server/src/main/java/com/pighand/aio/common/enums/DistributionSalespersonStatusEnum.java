package com.pighand.aio.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import com.pighand.framework.spring.base.BaseEnum;
import lombok.Getter;

/**
 * 分销 - 销售资格 - 状态
 */
@Getter
public enum DistributionSalespersonStatusEnum implements BaseEnum {
    /**
     * 禁用
     */
    DISABLED(0),

    /**
     * 启用
     */
    ENABLED(10);

    @JsonValue
    @EnumValue
    private final Integer value;

    DistributionSalespersonStatusEnum(int value) {
        this.value = value;
    }
}
