package com.pighand.aio.common.sdk.wechat;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.Map;

/**
 * 微信公众号
 *
 * @author wangshuli
 * @description <a
 * href="https://developers.weixin.qq.com/doc/offiaccount/Getting_Started/Overview.html">doc</a>
 */
@HttpExchange(value = "https://api.weixin.qq.com/", accept = "application/json", contentType = "application/json")
public interface MiniApplet {

    /**
     * 使用code获取access_token
     *
     * @param appid
     * @param secret
     * @param js_code
     * @return AccessToken
     * @see <a
     * href="https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/user-login/code2Session.html">doc</a>
     */
    @GetExchange("sns/jscode2session")
    String code2Session(@RequestParam String appid, @RequestParam String secret, @RequestParam String js_code,
        @RequestParam(defaultValue = "authorization_code") String grant_type);

    @PostExchange("cgi-bin/stable_token")
    String accessToken(@RequestBody Map<String, String> params);

    @PostExchange("wxa/business/getuserphonenumber")
    String getPhone(@RequestParam String access_token, @RequestBody Map<String, String> params);

    /**
     * 小程序码
     *
     * @param access_token
     * @param params       scene:参数 page:页面 env_version:正式版为 "release"，体验版为 "trial"，开发版为 "develop"
     * @return
     */
    @PostExchange("wxa/getwxacodeunlimit")
    byte[] getAppletQrcode(@RequestParam String access_token, @RequestBody Map<String, String> params);
}
