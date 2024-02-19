package com.pighand.aio.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import com.pighand.framework.spring.base.BaseEnum;
import lombok.Getter;

/**
 * 电商 - 商品 - 状态
 */
@Getter
public enum GoodsSpuStatusEnum implements BaseEnum {
    /**
     * 上架
     */
    LISTED(10),

    /**
     * 下架
     */
    UNLISTED(20);

    @JsonValue
    @EnumValue
    private final Integer value;

    GoodsSpuStatusEnum(int value) {
        this.value = value;
    }

}
