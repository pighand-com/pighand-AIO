package com.pighand.aio.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import com.pighand.framework.spring.base.BaseEnum;
import lombok.Getter;

/**
 * 电商 - 钱包 - 账单类型
 */
@Getter
public enum WalletBillTypeEnum implements BaseEnum {
    /**
     * 充值
     */
    RECHARGE(10),

    /**
     * 购物赠送
     */
    SHOPPING_GIFT(11),

    /**
     * 消费
     */
    CONSUMPTION(20),

    /**
     * 转账-转入
     */
    TRANSFER_IN(31),

    /**
     * 转账-转出
     */
    TRANSFER_OUT(32);

    @JsonValue
    @EnumValue
    private final Integer value;

    WalletBillTypeEnum(int value) {
        this.value = value;
    }
}
