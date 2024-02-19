package com.pighand.aio.common.sdk.douyin;

import com.pighand.aio.common.sdk.douyin.entity.AccessToken;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

/**
 * 抖音小游戏
 *
 * @author wangshuli
 */
//@HttpExchange(
//        value = "https://minigame.zijieapi.com/mgplatform/api/apps/",
//        accept = "application/json",
//        contentType = "application/json")
public interface MiniGame {

    /**
     * 小游戏解析CODE
     *
     * @param appid
     * @param secret
     * @param code
     * @param anonymousCode
     * @return UserInfo
     */
    @GetExchange("code2session")
    AccessToken code2session(@RequestParam String appid, @RequestParam String secret, @RequestParam String code,
        @RequestParam String anonymousCode);
}
