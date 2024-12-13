package com.pighand.aio.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import com.mybatisflex.annotation.EnumValue;

/**
 * @author wangshuli
 */
public enum PlatformEnum {
    // 系统直接注册
    SYSTEM_SIGN_IN("def"),

    // 微信 - 公众号
    WECHAT_OFFICIALLY_ACCOUNT("wechat_official_account"),

    // 微信 - 小程序
    WECHAT_APPLET("wechat_applet"),

    // 抖音小游戏
    DOUYIN_MINI_GAME("douyin_mini_game"),

    // 腾讯云  - cos
    TENCENT_CLOUD_COS("tencent_cloud_cos");

    @JsonValue
    @EnumValue
    public final String value;

    PlatformEnum(String value) {
        this.value = value;
    }
}
