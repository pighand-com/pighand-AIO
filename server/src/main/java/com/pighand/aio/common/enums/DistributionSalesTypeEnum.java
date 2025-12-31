package com.pighand.aio.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import com.pighand.framework.spring.base.BaseEnum;
import lombok.Getter;

/**
 * 分销 - 销售记录 - 类型
 */
@Getter
public enum DistributionSalesTypeEnum implements BaseEnum {
    /**
     * 销售单
     */
    SALES_ORDER(10),

    /**
     * 结算单
     */
    SETTLEMENT_ORDER(20);

    @JsonValue
    @EnumValue
    private final Integer value;

    DistributionSalesTypeEnum(int value) {
        this.value = value;
    }
}
