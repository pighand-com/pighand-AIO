package com.pighand.aio.common.sdk.wechat;

import com.pighand.aio.common.sdk.wechat.entity.AccessToken;
import com.pighand.aio.common.sdk.wechat.entity.UserInfo;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

/**
 * 微信公众号
 *
 * @author wangshuli
 * @description <a
 * href="https://developers.weixin.qq.com/doc/offiaccount/Getting_Started/Overview.html">doc</a>
 */
@HttpExchange(value = "https://api.weixin.qq.com/", accept = "application/json", contentType = "application/json")
public interface OfficiallyAccount {

    /**
     * 使用code获取access_token
     *
     * @param appid
     * @param secret
     * @param code
     * @return AccessToken
     * @see <a
     * href="https://developers.weixin.qq.com/doc/offiaccount/OA_Web_Apps/Wechat_webpage_authorization.html#1">doc</a>
     */
    @GetExchange("sns/oauth2/access_token")
    AccessToken accessToken(@RequestParam String appid, @RequestParam String secret, @RequestParam String code,
        @RequestParam(defaultValue = "authorization_code") String grant_type);

    /**
     * 公众号查询用户信息
     *
     * @param accessToken
     * @param openid
     * @return UserInfo
     * @see <a
     * href="https://developers.weixin.qq.com/doc/offiaccount/OA_Web_Apps/Wechat_webpage_authorization.html#3">doc</a>
     */
    @GetExchange("sns/userinfo")
    UserInfo userinfo(@RequestParam String accessToken, @RequestParam String openid);
}
