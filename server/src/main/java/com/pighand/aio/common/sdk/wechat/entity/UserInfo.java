package com.pighand.aio.common.sdk.wechat.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 公众平台 - userinfo
 *
 * @author wangshuli
 */
@Data
public class UserInfo extends WechatResponse {

    @JsonProperty("openid")
    private String openid;

    @JsonProperty("nickname")
    private String nickname;

    @JsonProperty("sex")
    private Integer sex;

    @JsonProperty("province")
    private String province;

    @JsonProperty("city")
    private String city;

    @JsonProperty("country")
    private String country;

    @JsonProperty("headimgurl")
    private String headimgurl;

    @JsonProperty("unionid")
    private String unionid;

    @JsonProperty("session_key")
    private String sessionKey;

    public UserInfo() {
        super("公众号查询用户信息");
    }
}
