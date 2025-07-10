package com.pighand.aio.common.sdk.wechat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 微信SDK
 *
 * @author wangshuli
 */
@Service
public class WechatSDK {

    public static Utils utils;

    /**
     * 公众平台
     */
    public static OfficiallyAccount OFFICIALLY_ACCOUNT;

    /**
     * 小程序
     */
    public static MiniApplet MINI_APPLET;

    public WechatSDK() {
    }

    @Autowired(required = false)
    public WechatSDK(Utils utils, OfficiallyAccount officiallyAccount, MiniApplet miniApplet) {
        WechatSDK.utils = utils;
        WechatSDK.OFFICIALLY_ACCOUNT = officiallyAccount;
        WechatSDK.MINI_APPLET = miniApplet;
    }
}
