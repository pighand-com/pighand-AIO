package com.pighand.aio.common.sdk.douyin.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * 公众平台 - userinfo
 *
 * @author wangshuli
 */
@Data
public class AccessToken extends DouyinResponse {

    @JsonProperty("openid")
    private String openid;

    @JsonProperty("anonymous_openid")
    private String anonymousOpenid;

    @JsonProperty("unionid")
    private String unionid;

    @JsonProperty("session_key")
    private String sessionKey;

    public AccessToken() {
        super("公众号查询用户信息");
    }
}
