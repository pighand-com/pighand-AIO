package com.pighand.aio.service.base.tripartite.douyin;

import com.pighand.aio.common.enums.PlatformEnum;
import com.pighand.aio.common.sdk.douyin.entity.AccessToken;
import com.pighand.aio.domain.base.ApplicationPlatformKeyDomain;
import com.pighand.aio.service.base.ApplicationPlatformKeyService;
import com.pighand.aio.service.base.TripartitePlatformService;
import com.pighand.aio.vo.base.tripartite.EncryptedData;
import com.pighand.aio.vo.base.tripartite.TripartitePlatformUserInfo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 抖音 - 小游戏
 *
 * @author wangshuli
 */
public class MiniGameImpl extends AbstractDouyin<AccessToken> implements TripartitePlatformService {

    @Autowired
    private ApplicationPlatformKeyService platformKeyService;

    MiniGameImpl() {
        super(PlatformEnum.DOUYIN_MINI_GAME);
    }

    /**
     * 解析code
     */
    @Override
    protected AccessToken analysisCode(Long applicationId, String code, String anonymousCode) {
        ApplicationPlatformKeyDomain key = platformKeyService.findByPlatform(PlatformEnum.DOUYIN_MINI_GAME);

        //        return DouyinSDK.MINI_GAME.code2session(key.getAppid(), key.getSecret(), code, anonymousCode);
        return null;
    }

    /**
     * 获取用户平台信息
     */
    @Override
    protected TripartitePlatformUserInfo getPlatformUserInfo(AccessToken analysisInfo) {
        return new TripartitePlatformUserInfo();
    }

    @Override
    protected String bindPhone(Long applicationId, EncryptedData encryptedData) {
        return "";
    }
}
