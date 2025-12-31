package com.pighand.aio.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;
import com.pighand.framework.spring.base.BaseEnum;
import lombok.Getter;

/**
 * 电商 - 钱包 - 入账类型
 */
@Getter
public enum WalletBillWalletTypeEnum implements BaseEnum {
    /**
     * 余额
     */
    BALANCE(10),

    /**
     * 代币
     */
    TOKEN(20);

    @JsonValue
    @EnumValue
    private final Integer value;

    WalletBillWalletTypeEnum(int value) {
        this.value = value;
    }
}
