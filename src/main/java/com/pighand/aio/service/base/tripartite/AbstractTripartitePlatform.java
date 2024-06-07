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
import com.pighand.aio.vo.base.UserMessageVO;
import com.pighand.aio.vo.base.UserVO;
import com.pighand.aio.vo.base.tripartite.EncryptedData;
import com.pighand.aio.vo.base.tripartite.TripartitePlatformUserInfo;
import com.pighand.aio.vo.base.tripartite.UserPlatformInfo;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

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
     * @param projectId     项目id
     * @param code          三方平台code
     * @param anonymousCode 匿名code
     * @return T
     */
    protected abstract T analysisCode(Long projectId, String code, String anonymousCode);

    /**
     * 校验用户在库中是否存在
     *
     * @param projectId    项目id
     * @param analysisInfo CODE解析信息
     * @return userPlatformInfo {@link UserPlatformInfo}
     */
    protected abstract UserPlatformInfo checkUserExist(Long projectId, T analysisInfo);

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
     * @param projectId
     * @param encryptedData
     * @return
     */
    protected abstract String bindPhone(Long projectId, EncryptedData encryptedData);

    /**
     * 同步用户平台信息
     *
     * <p>创建或更新
     *
     * @param projectId        项目id
     * @param analysisInfo     CODE解析信息
     * @param userPlatformInfo 用户平台信息
     * @param newUserStatus    新用户状态
     */
    protected abstract void syncPlatform(Long projectId, T analysisInfo, UserPlatformInfo userPlatformInfo,
        UserStatusEnum newUserStatus);

    /**
     * 使用code登录
     *
     * @see #signInByCode(Long, String, String)
     */
    public UserVO signInByCode(Long projectId, String code, Long roleId) {
        return this.signInByCode(projectId, code, null, roleId);
    }

    /**
     * 使用code登录
     *
     * @see #signInByCode(Long, String, String)
     */
    public UserVO signInByCode(Long projectId, String code) {
        return this.signInByCode(projectId, code, null, null);
    }

    /**
     * 使用code登录
     *
     * @see #signInByCode(Long, String, String)
     */
    public UserVO signInByCode(Long projectId, String code, String anonymousCode) {
        return this.signInByCode(projectId, code, anonymousCode, null);
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
     * @param projectId     项目id
     * @param code          三方平台code
     * @param anonymousCode 匿名code
     * @return token
     */
    public UserVO signInByCode(Long projectId, String code, String anonymousCode, Long roleId) {
        // 1. 解析CODE
        T analysisInfo = this.analysisCode(projectId, code, anonymousCode);

        // 2. 用户是否存在
        UserPlatformInfo userPlatformInfo = this.checkUserExist(projectId, analysisInfo);
        boolean isUserExist = userPlatformInfo != null && userPlatformInfo.getUserId() != null;

        UserVO userInfo = null;

        // 2.1 创建用户
        UserStatusEnum newUserStatus = null;
        if (!isUserExist) {
            UserVO userVO = new UserVO();
            userVO.setProjectId(projectId);
            userVO.setInitialSourcePlatform(this.userSourcePlatform);

            if (roleId == null) {
                roleId = RoleEnum.USER.value;
            }
            userVO.setRoleId(roleId);

            userInfo = userService.create(userVO);

            // 发送站内信
            // TODO 抽出来
            UserMessageVO messageVO = new UserMessageVO();
            messageVO.setType(UserMessageTypeEnum.SYSTEM);
            messageVO.setReceiverId(userInfo.getId());
            messageVO.setTitle("来自2077年的问候");
            messageVO.setContent("你是谁？\n"
                + "公元2023年，富甲一方的你厌倦了枯燥乏味的生活，将大部分资产兑换成黄金，藏匿在只有你自己知道的地方后，便与其他富豪们一起加入了未来先驱公司的【冬眠计划】——将自己冷冻100年，等待2123年重启未来......\n"
                + "\n" + "你在哪里？\n" + "未来先驱公司在2077年全球核战中宣布破产，万幸的是【冬眠中心】并没有被摧毁，公司提前46年将你们解冻复苏。于是，【夜之城】便成为你苏醒后的新家园。\n"
                + "\n" + "你的处境？\n" + "你苏醒后身体出现了各种不适应症，机能退化导致你短暂性失忆，这就意味着：你的黄金，不见了！曾经富可敌国的你，身无分文的流落于夜之城的街头。\n"
                + "\n" + "你该怎么办？\n"
                + "听说夜之城黑市的酒吧里，有一款【唤醒剂】能使你恢复记忆。你必须想办法混入【瘾酒游戏】派对现场，尽一切可能获得更多的【唤醒剂】。每获得1毫升的【唤醒剂】，就等于你解锁了1公斤的黄金。要知道，这将是你重回社会顶层的唯一方法。\n"
                + "\n" + "你今晚的任务？\n" + "参加派对，加入夜之城的帮派，想办法获得大量的【唤醒剂】，解锁你脑海中隐藏的黄金，成为夜之城最富有的人。\n"
                + "★【1毫升唤醒剂】=【1公斤黄金】；\n" + "★被解锁的黄金，将进入你的“新世界银行”账户中；\n"
                + "★黄金可以真实抵扣使用，每公斤黄金可抵扣约人民币100.00元。");
            messageVO.setSendAt(new Date());
            messageVO.setRead(false);
            userMessageMapper.insert(messageVO);

            messageVO = new UserMessageVO();
            messageVO.setType(UserMessageTypeEnum.SYSTEM);
            messageVO.setReceiverId(userInfo.getId());
            messageVO.setTitle("【瘾酒游戏】必读攻略");
            messageVO.setContent(
                "什么是【黄金榜】？\n" + "黄金榜是【瘾酒游戏】派对现场，各个帮派之间争夺的榜单。获得榜首的帮派，将得到奖励。其他帮派将受到对应惩罚。黄金榜以帮派人均黄金量为计算标准：\n"
                    + "【帮派黄金总量 ÷ 帮派总人数 = 黄金榜数值】\n" + "\n" + "什么是【酒尊】竞选？\n"
                    + "酒尊是【瘾酒游戏】派对现场，通过公开竞争产生的一项特权身份，特权如下：\n" + "1、获得USD100.00奖励\n"
                    + "2、无限次任意5折消费【需酒尊本人交易】\n" + "3、选择与他人PK，对方无权拒绝【需酒尊本人参与】\n"
                    + "4、选择与他人PK，失利后仅需饮用一半【需酒尊本人参与】\n" + "\n" + "你该如何获得【黄金】？\n"
                    + "1、购买【唤醒剂】即获得黄金【1ml唤醒剂=1公斤黄金】\n" + "2、从超级刮刮卡中赢得【最高可获得50ml唤醒剂】\n"
                    + "3、与他人进行黄金转账\n" + "4、与他人PK赢得\n" + "\n" + "你该如何获得【USD游戏货币】？\n"
                    + "1、挑战瘾酒擂台获得【最高可获得USD60.00】\n" + "2、舞台秀挑战赢得【最高可获得USD100.00】\n"
                    + "3、从超级刮刮卡中赢得【最高可获得USD20.00】\n" + "4、集齐五色LUCKY币获得【可获得USD50.00】\n"
                    + "5、与他人PK赢得\n" + "6、从对赌游戏中赢得\n" + "7、完成帮派任务后获得\n" + "\n" + "你还可以这么玩！\n"
                    + "1、请根据自身情况，适量饮酒\n" + "2、需饮酒时，可向现场好友求助代饮\n"
                    + "3、需饮酒时，可使用USD进行抵扣。【USD 20.00抵扣1罐 】\n"
                    + "4、需饮酒时，可以使用【唤醒剂】抵扣。【20毫升唤醒剂抵扣1罐】");
            messageVO.setSendAt(new Date());
            messageVO.setRead(false);
            userMessageMapper.insert(messageVO);

            userPlatformInfo.setUserId(userInfo.getId());
        } else {
            userInfo = userService.find(userPlatformInfo.getUserId());

        }

        // 3. 同步用户平台数据
        this.syncPlatform(projectId, analysisInfo, userPlatformInfo, newUserStatus);

        // 4. 查询三方平台数据
        TripartitePlatformUserInfo platFormUserInfo = this.getPlatformUserInfo(analysisInfo);

        // 5. 同步扩展数据
        UserExtensionDomain userExtensionDomain = userExtensionService.getById(userInfo.getId());
        if (userExtensionDomain == null) {
            userExtensionDomain = new UserExtensionDomain();
            userExtensionDomain.setId(userInfo.getId());
            userExtensionDomain.setProjectId(projectId);
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
     * @param projectId
     * @param userId
     * @param code
     */
    public String bindPhone(Long projectId, Long userId, String code) {
        EncryptedData encryptedData = new EncryptedData();
        encryptedData.setCode(code);

        String phone = this.bindPhone(projectId, encryptedData);

        UserDomain userDomain = new UserDomain();
        userDomain.setId(userId);
        userDomain.setPhone(phone);
        userService.updateById(userDomain);

        return phone;
    }
}
