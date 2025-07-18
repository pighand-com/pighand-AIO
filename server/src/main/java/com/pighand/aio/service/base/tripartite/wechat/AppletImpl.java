package com.pighand.aio.service.base.tripartite.wechat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pighand.aio.common.enums.PlatformEnum;
import com.pighand.aio.common.sdk.wechat.WechatSDK;
import com.pighand.aio.common.sdk.wechat.entity.UserInfo;
import com.pighand.aio.domain.base.ApplicationPlatformKeyDomain;
import com.pighand.aio.service.base.ApplicationPlatformKeyService;
import com.pighand.aio.service.base.TripartitePlatformService;
import com.pighand.aio.vo.base.tripartite.EncryptedData;
import com.pighand.aio.vo.base.tripartite.TripartitePlatformUserInfo;
import com.pighand.framework.spring.exception.ThrowException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 微信 - 公众号
 *
 * @author wangshuli
 * @see <a
 * href="https://developers.weixin.qq.com/doc/offiaccount/OA_Web_Apps/Wechat_webpage_authorization.html">doc</a>
 */
@Service
public class AppletImpl extends AbstractWechat<UserInfo> implements TripartitePlatformService {

    ObjectMapper om = new ObjectMapper();

    @Autowired
    private ApplicationPlatformKeyService platformKeyService;

    AppletImpl() {
        super(PlatformEnum.WECHAT_APPLET);
    }

    /**
     * 解析code
     */
    @Override
    protected UserInfo analysisCode(Long applicationId, String code, String anonymousCode) {
        ApplicationPlatformKeyDomain key = platformKeyService.findByPlatform(PlatformEnum.WECHAT_APPLET);

        String result = WechatSDK.MINI_APPLET.code2Session(key.getAppid(), key.getSecret(), code, null);

        // 使用Jackson将result转换为UserInfo
        UserInfo userInfo = null;
        try {
            userInfo = om.readValue(result, UserInfo.class);
        } catch (JsonProcessingException e) {
            throw new ThrowException("解析微信返回的code2Session结果失败");
        }

        return userInfo;
    }

    /**
     * 获取用户平台信息
     */
    @Override
    protected TripartitePlatformUserInfo getPlatformUserInfo(UserInfo analysisInfo) {
        return new TripartitePlatformUserInfo();
    }

    @Override
    protected String bindPhone(Long applicationId, EncryptedData encryptedData) {
        ApplicationPlatformKeyDomain key = platformKeyService.findByPlatform(PlatformEnum.WECHAT_APPLET);

        // TODO: 统一管理
        String result = WechatSDK.MINI_APPLET.accessToken(key.getAppid(), key.getSecret(), "client_credential");

        String patternString = "\"access_token\":\"(.*?)\"";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(result);

        String token = "";
        if (matcher.find()) {
            token = matcher.group(1);
        } else {
            throw new ThrowException("解析微信返回的accessToken失败");
        }

        HashMap params = new HashMap<>(1);
        params.put("code", encryptedData.getCode());

        HashMap headers = new HashMap<>(1);
        String body = new ObjectMapper().valueToTree(params).toString();
        headers.put("Content-Length", String.valueOf(body.getBytes(StandardCharsets.UTF_8).length));

        String userInfo = WechatSDK.MINI_APPLET.getPhone(token, params, headers);

        patternString = "\"phoneNumber\":\"(.*?)\"";
        pattern = Pattern.compile(patternString);
        matcher = pattern.matcher(userInfo);

        if (matcher.find()) {
            return matcher.group(1);
        } else {
            throw new ThrowException("解析微信返回的phoneNumber失败");
        }
    }
}
