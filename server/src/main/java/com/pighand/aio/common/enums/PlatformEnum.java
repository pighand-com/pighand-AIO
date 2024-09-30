package com.pighand.aio.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;

/**
 * @author wangshuli
 */
public enum PlatformEnum {
    // 系统直接注册
    SYSTEM_SIGN_IN(10),

    // 微信 - 公众号
    WECHAT_OFFICIALLY_ACCOUNT(21),

    // 微信 - 小程序
    WECHAT_MINI_PROGRAM(22),

    // 抖音小游戏
    DOUYIN_MINI_GAME(33),

    // 腾讯云  - cos
    TENCENT_CLOUD_COS(101);

    @JsonValue
    @EnumValue
    public final int value;

    PlatformEnum(int value) {
        this.value = value;
    }
}
