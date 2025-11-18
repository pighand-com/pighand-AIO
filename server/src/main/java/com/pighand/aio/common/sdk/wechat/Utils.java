package com.pighand.aio.common.sdk.wechat;

import com.pighand.framework.spring.exception.ThrowException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Utils {

    @Value("${spring.profiles.active}")
    private String env;

    /**
     * 解析微信返回的accessToken
     *
     * @param accessToken
     * @return
     */
    public String extractAccessToken(String accessToken) {
        String patternString = "\"access_token\":\"(.*?)\"";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(accessToken);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new ThrowException("解析微信返回的accessToken失败");
        }
    }

    /**
     * 根据环境变量获取微信小程序环境
     *
     * @return release, trial, developer
     */
    public String getAppletEnv() {
        return switch (env) {
            //            case "dev" -> "developer";
            case "dev" -> "trial";
            case "test" -> "trial";
            default -> "release";
        };
    }
}
