package com.pighand.aio.service.base.tripartite;

import com.pighand.aio.common.enums.PlatformEnum;
import com.pighand.aio.common.enums.RoleEnum;
import com.pighand.aio.common.enums.UserMessageTypeEnum;
import com.pighand.aio.common.enums.UserStatusEnum;
import com.pighand.aio.domain.base.UserDomain;
import com.pighand.aio.domain.base.UserExtensionDomain;
import com.pighand.aio.mapper.base.UserMessageMapper;
import com.pighand.aio.service.base.AuthorizationService;
import com.pighand.aio.service.base.UserExtensionService;
import com.pighand.aio.service.base.UserService;
import com.pighand.aio.service.base.UserStationLetterService;
import com.pighand.aio.vo.base.UserMessageVO;
import com.pighand.aio.vo.base.UserStationLetterVO;
import com.pighand.aio.vo.base.UserVO;
import com.pighand.aio.vo.base.tripartite.EncryptedData;
import com.pighand.aio.vo.base.tripartite.TripartitePlatformUserInfo;
import com.pighand.aio.vo.base.tripartite.UserPlatformInfo;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

import static com.pighand.aio.common.dataPermission.ignore.RunWithIgnore.IgnoreDataPermission;

/**
 * 三方平台抽象类
 *
 * <p>T (interface): 三方平台解析code返回的参数格式
 *
 * <p>子类需实现TripartitePlatformService接口
 *
 * @author wangshuli
 */
@Service
public abstract class AbstractTripartitePlatform<T> {

    @Getter
    private final PlatformEnum userSourcePlatform;

    @Autowired
    private UserService userService;

    @Autowired
    private UserExtensionService userExtensionService;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private UserStationLetterService userStationLetterService;

    @Autowired
    private UserMessageMapper userMessageMapper;

    protected AbstractTripartitePlatform() {
        throw new UnsupportedOperationException("Subclasses must call the constructor with a PlatformEnum parameter.");
    }

    public AbstractTripartitePlatform(PlatformEnum userSourcePlatform) {
        this.userSourcePlatform = userSourcePlatform;
    }

    /**
     * 解析code
     *
     * @param applicationId 项目id
     * @param code          三方平台code
     * @param anonymousCode 匿名code
     * @return T
     */
    protected abstract T analysisCode(Long applicationId, String code, String anonymousCode);

    /**
     * 校验用户在库中是否存在
     *
     * @param applicationId 项目id
     * @param analysisInfo  CODE解析信息
     * @return userPlatformInfo {@link UserPlatformInfo}
     */
    protected abstract UserPlatformInfo checkUserExist(Long applicationId, T analysisInfo);

    /**
     * 获取用户平台信息
     *
     * @param analysisInfo CODE解析信息
     * @return tripartitePlatformUserInfo {@link TripartitePlatformUserInfo}
     */
    protected abstract TripartitePlatformUserInfo getPlatformUserInfo(T analysisInfo);

    /**
     * 绑定手机号
     *
     * @param applicationId
     * @param encryptedData
     * @return
     */
    protected abstract String bindPhone(Long applicationId, EncryptedData encryptedData);

    /**
     * 同步用户平台信息
     *
     * <p>创建或更新
     *
     * @param applicationId    项目id
     * @param analysisInfo     CODE解析信息
     * @param userPlatformInfo 用户平台信息
     * @param newUserStatus    新用户状态
     */
    protected abstract void syncPlatform(Long applicationId, T analysisInfo, UserPlatformInfo userPlatformInfo,
        UserStatusEnum newUserStatus);

    /**
     * 使用code登录
     *
     * @see #loginInByCode(Long, String, String)
     */
    public UserVO loginInByCode(Long applicationId, String code, Long roleId) {
        return this.loginInByCode(applicationId, code, null, roleId);
    }

    /**
     * 使用code登录
     *
     * @see #loginInByCode(Long, String, String)
     */
    public UserVO loginInByCode(Long applicationId, String code) {
        return this.loginInByCode(applicationId, code, null, null);
    }

    /**
     * 使用code登录
     *
     * @see #loginInByCode(Long, String, String)
     */
    public UserVO loginInByCode(Long applicationId, String code, String anonymousCode) {
        return this.loginInByCode(applicationId, code, anonymousCode, null);
    }

    /**
     * 使用code登录
     *
     * <p>1. 解析code中的平台信息（openid、unionid等）
     *
     * <p>2. 使用平台id查询用户是否存在；不存在则创建
     *
     * <p>3. 创建或更新平台用户表数据
     *
     * <p>4. 查询三方平台的用户信息
     *
     * <p>5. 使用三方平台的用户信息，创建或更新用户扩展数据
     *
     * @param applicationId 项目id
     * @param code          三方平台code
     * @param anonymousCode 匿名code
     * @return token
     */
    public UserVO loginInByCode(Long applicationId, String code, String anonymousCode, Long roleId) {
        // 1. 解析CODE
        T analysisInfo = this.analysisCode(applicationId, code, anonymousCode);

        // 2. 用户是否存在
        UserPlatformInfo userPlatformInfo = this.checkUserExist(applicationId, analysisInfo);
        boolean isUserExist = userPlatformInfo != null && userPlatformInfo.getUserId() != null;

        UserVO userInfo = null;

        // 2.1 创建用户
        UserStatusEnum newUserStatus = null;
        if (!isUserExist) {
            UserVO userVO = new UserVO();
            userVO.setInitialSourcePlatform(this.userSourcePlatform);

            if (roleId == null) {
                roleId = RoleEnum.USER.value;
            }
            userVO.setRoleId(roleId);

            userInfo = userService.create(userVO);

            // 发送站内信
            List<UserStationLetterVO> stationLetters = userStationLetterService.queryByApplicationId(applicationId);
            if (stationLetters != null && !stationLetters.isEmpty()) {
                for (UserStationLetterVO letter : stationLetters) {
                    UserMessageVO messageVO = new UserMessageVO();
                    messageVO.setType(UserMessageTypeEnum.SYSTEM);
                    messageVO.setReceiverId(userInfo.getId());
                    messageVO.setTitle(letter.getTitle());
                    messageVO.setContent(letter.getContent());
                    messageVO.setSendAt(new Date());
                    messageVO.setRead(false);
                    userMessageMapper.insert(messageVO);
                }
            }

            userPlatformInfo.setUserId(userInfo.getId());
        } else {
            userInfo = IgnoreDataPermission(() -> userService.find(userPlatformInfo.getUserId()));
        }

        // 3. 同步用户平台数据
        this.syncPlatform(applicationId, analysisInfo, userPlatformInfo, newUserStatus);

        // 4. 查询三方平台数据
        TripartitePlatformUserInfo platFormUserInfo = this.getPlatformUserInfo(analysisInfo);

        // 5. 同步扩展数据
        UserExtensionDomain userExtensionDomain = userExtensionService.getById(userInfo.getId());
        if (userExtensionDomain == null) {
            userExtensionDomain = new UserExtensionDomain();
            userExtensionDomain.setId(userInfo.getId());
            userExtensionDomain.setNickname(platFormUserInfo.getNickname());
            userExtensionDomain.setProfile(platFormUserInfo.getProfile());
            userExtensionDomain.setGender(platFormUserInfo.getGender());
            userExtensionService.create(userExtensionDomain);
        }
        userInfo.setExtension(userExtensionDomain);

        return authorizationService.generateToken(userInfo);
    }

    /**
     * 绑定手机号
     *
     * @param applicationId
     * @param userId
     * @param code
     */
    public String bindPhone(Long applicationId, Long userId, String code) {
        EncryptedData encryptedData = new EncryptedData();
        encryptedData.setCode(code);

        String phone = this.bindPhone(applicationId, encryptedData);

        UserDomain userDomain = new UserDomain();
        userDomain.setId(userId);
        userDomain.setPhone(phone);
        userService.updateById(userDomain);

        return phone;
    }
}
