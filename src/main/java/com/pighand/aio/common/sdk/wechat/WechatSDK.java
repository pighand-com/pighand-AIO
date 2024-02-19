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
    /**
     * 公众平台
     */
    public static OfficiallyAccount OFFICIALLY_ACCOUNT;

    public static MiniApplet MINI_APPLET;

    @Autowired(required = false)
    public WechatSDK(OfficiallyAccount officiallyAccount, MiniApplet MINI_APPLET) {
        WechatSDK.OFFICIALLY_ACCOUNT = officiallyAccount;
        WechatSDK.MINI_APPLET = MINI_APPLET;
    }
}
