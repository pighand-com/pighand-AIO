package com.pighand.aio.service.eCommerce.impl;

import com.pighand.aio.common.enums.PlatformEnum;
import com.pighand.aio.domain.eCommerce.SessionUserGroupDomain;
import com.pighand.aio.domain.project.ProjectPlatformKeyDomain;
import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.mapper.eCommerce.SessionUserGroupMapper;
import com.pighand.aio.common.sdk.wechat.WechatSDK;
import com.pighand.aio.service.eCommerce.SessionUserGroupService;
import com.pighand.aio.service.project.ProjectPlatformKeyService;
import com.pighand.aio.vo.eCommerce.SessionUserGroupVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.exception.ThrowException;
import com.pighand.framework.spring.page.PageOrList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 电商 - 场次 - 用户分组
 *
 * @author wangshuli
 * @createDate 2023-12-05 16:13:27
 */
@Service
public class SessionUserGroupServiceImpl extends BaseServiceImpl<SessionUserGroupMapper, SessionUserGroupDomain>
    implements SessionUserGroupService {

    @Autowired
    private ProjectPlatformKeyService projectPlatformKeyService;

    @Value("spring.profiles.active")
    private String env;

    /**
     * 创建
     *
     * @param sessionUserGroupVO
     * @return
     */
    @Override
    public SessionUserGroupVO create(SessionUserGroupVO sessionUserGroupVO) {
        super.mapper.insert(sessionUserGroupVO);

        return sessionUserGroupVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public SessionUserGroupDomain find(Long id) {
        return super.mapper.find(id, null);
    }

    /**
     * 分页或列表
     *
     * @param sessionUserGroupVO
     * @return PageOrList<SessionUserGroupVO>
     */
    @Override
    public PageOrList<SessionUserGroupVO> query(SessionUserGroupVO sessionUserGroupVO) {

        return super.mapper.query(sessionUserGroupVO);
    }

    /**
     * 修改
     *
     * @param sessionUserGroupVO
     */
    @Override
    public void update(SessionUserGroupVO sessionUserGroupVO) {
        super.mapper.update(sessionUserGroupVO);
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        super.mapper.deleteById(id);
    }

    /**
     * 获取小程序码
     *
     * @param sessionGroupId
     */
    @Override
    public String getWechatAppletQrcode(Long sessionGroupId) {
        ProjectPlatformKeyDomain key =
            projectPlatformKeyService.findByPlatform(Context.getProjectId(), PlatformEnum.WECHAT_MINI_PROGRAM);

        Map<String, String> params = new HashMap<>();
        params.put("appid", key.getAppid());
        params.put("secret", key.getSecret());
        params.put("grant_type", "client_credential");
        String accessToken = WechatSDK.MINI_APPLET.accessToken(params);

        String patternString = "\"access_token\":\"(.*?)\"";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(accessToken);

        String token = "";
        if (matcher.find()) {
            token = matcher.group(1);
        } else {
            throw new ThrowException("解析微信返回的accessToken失败");
        }

        String qrcodeEnv = "release";
        switch (env) {
            case "dev":
                qrcodeEnv = "develop";
                break;
            case "test":
                qrcodeEnv = "trial";
                break;
            default:
                qrcodeEnv = "release";
                break;
        }

        params = new HashMap<>();
        params.put("scene", sessionGroupId.toString());
        params.put("env_version", qrcodeEnv);
        byte[] images = WechatSDK.MINI_APPLET.getAppletQrcode(token, params);

        return Base64.getEncoder().encodeToString(images);
    }
}
