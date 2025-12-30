package com.pighand.aio.service.MKT;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.common.enums.PlatformEnum;
import com.pighand.aio.common.sdk.wechat.WechatSDK;
import com.pighand.aio.domain.MKT.LotteryCommonConfigDomain;
import com.pighand.aio.domain.MKT.LotteryCommonUserDomain;
import com.pighand.aio.domain.MKT.LotteryParticipatePrizeDomain;
import com.pighand.aio.domain.base.ApplicationPlatformKeyDomain;
import com.pighand.aio.domain.base.UserWechatDomain;
import com.pighand.aio.mapper.MKT.LotteryCommonConfigMapper;
import com.pighand.aio.service.MKT.LotteryCommonUserService;
import com.pighand.aio.service.MKT.lotteryType.LotteryTypeService;
import com.pighand.aio.service.base.ApplicationPlatformKeyService;
import com.pighand.aio.service.base.UserWechatService;
import com.pighand.aio.service.common.UploadService;
import com.pighand.aio.service.msg.NotifyConfigWechatAppletService;
import com.pighand.aio.vo.MKT.LotteryCommonUserVO;
import com.pighand.aio.vo.MKT.LotteryParticipateVO;
import com.pighand.aio.vo.MKT.LotteryVO;
import com.pighand.aio.vo.msg.NotifyConfigWechatAppletVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static com.pighand.aio.domain.MKT.table.LotteryCommonConfigTableDef.LOTTERY_COMMON_CONFIG;
import static com.pighand.aio.domain.MKT.table.LotteryCommonUserTableDef.LOTTERY_COMMON_USER;
import static com.pighand.aio.domain.base.table.UserWechatTableDef.USER_WECHAT;
import static com.pighand.aio.domain.msg.table.NotifyConfigWechatAppletTableDef.NOTIFY_CONFIG_WECHAT_APPLET;

/**
 * 营销 - 抽奖配置
 *
 * @author wangshuli
 * @createDate 2024-11-28 17:21:35
 */
@Service
@RequiredArgsConstructor
public class LotteryService extends BaseServiceImpl<LotteryCommonConfigMapper, LotteryCommonConfigDomain>
     {

    private final UserWechatService userWechatService;

    private final ApplicationPlatformKeyService projectPlatformKeyService;

    private final NotifyConfigWechatAppletService notifyConfigWechatAppletService;

    private final UploadService uploadService;

    private final LotteryCommonUserService lotteryCommonUserService;

    private final LotteryTypeService<LotteryParticipateVO, LotteryParticipatePrizeDomain> lotteryTypeParticipateService;

    private LotteryTypeService getLotteryTypeService(Integer type) {
        return switch (type) {
            case 10 -> lotteryTypeParticipateService;
            default -> throw new RuntimeException("不支持的抽奖类型: " + type);
        };
    }

    /**
     * 创建
     *
     * @param lotteryVO
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public LotteryVO create(LotteryVO lotteryVO) {
        lotteryVO.setDrawStatus(10);
        super.mapper.insert(lotteryVO);

        LotteryTypeService typeService = this.getLotteryTypeService(lotteryVO.getLotteryType());

        Object lotteryVObject = typeService.getLotteryObject(lotteryVO);
        List<Object> prizes = typeService.getLotteryPrizes(lotteryVObject);

        typeService.create(lotteryVO.getId(), lotteryVObject);
        typeService.createPrizes(lotteryVO.getId(), prizes);

        uploadService.updateFileOfficial(lotteryVO.getCoverUrl());

        // 设置同步信息
        notifyConfigWechatAppletService.syncByTemplate(100, lotteryVO.getId());

        return lotteryVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public LotteryVO find(Long id) {
        LotteryVO lotteryVO = super.mapper.find(id);

        LotteryTypeService typeService = this.getLotteryTypeService(lotteryVO.getLotteryType());

        Object lotteryVObject = typeService.find(id);
        List<Object> prizes = typeService.queryPrizes(id);

        typeService.setLotteryPrizes(lotteryVObject, prizes);
        typeService.setLotteryObject(lotteryVO, lotteryVObject);

        // 查询是否参加
        LotteryCommonUserDomain lotteryCommonUser = lotteryCommonUserService.isParticipate(id);
        lotteryVO.setIsParticipate(lotteryCommonUser != null);

        // 中奖信息
        if (lotteryCommonUser != null) {
            lotteryVO.setUserPrizeId(lotteryCommonUser.getPrizeId());
        }

        // 参与人数
        lotteryVO.setParticipateCount(lotteryCommonUserService.count(id));

        Date now = new Date();
        // TODO: 解决时间问题，库和代码必须时区相同，要不有问题
        Date localBegin = new Date(lotteryVO.getBeginTime().getTime() - 8 * 60 * 60 * 1000);
        Date localEnd = new Date(lotteryVO.getEndTime().getTime() - 8 * 60 * 60 * 1000);
        lotteryVO.setIsBegin(localBegin.before(now));
        lotteryVO.setIsEnd(localEnd.before(now));

        return lotteryVO;
    }

    /**
     * 分页或列表
     *
     * @param lotteryVO
     * @return PageOrList<lotteryVO>
     */
    public PageOrList<LotteryVO> query(LotteryVO lotteryVO) {

        QueryWrapper queryWrapper = QueryWrapper.create()

            // like
            .and(LOTTERY_COMMON_CONFIG.LOTTERY_TYPE.like(lotteryVO.getLotteryType()))
            .and(LOTTERY_COMMON_CONFIG.TITLE.like(lotteryVO.getTitle()))
            .and(LOTTERY_COMMON_CONFIG.SHARE_IMAGE_URL.like(lotteryVO.getShareImageUrl()))
            .and(LOTTERY_COMMON_CONFIG.HELP_POSTER_URL.like(lotteryVO.getHelpPosterUrl()))
            .and(LOTTERY_COMMON_CONFIG.MORE_BUTTON_URL.like(lotteryVO.getMoreButtonUrl()))
            .and(LOTTERY_COMMON_CONFIG.MORE_EXTERNAL_URL.like(lotteryVO.getMoreExternalUrl()))
            .and(LOTTERY_COMMON_CONFIG.RESULT_NOTIFY_URL.like(lotteryVO.getResultNotifyUrl()))

            // equal
            .and(LOTTERY_COMMON_CONFIG.HELP_ADD_BY_TIMES.eq(lotteryVO.getHelpAddByTimes()))
            .and(LOTTERY_COMMON_CONFIG.MAX_HELP_ME.eq(lotteryVO.getMaxHelpMe()))
            .and(LOTTERY_COMMON_CONFIG.MAX_HELP_OTHER.eq(lotteryVO.getMaxHelpOther()))
            .and(LOTTERY_COMMON_CONFIG.SHOW_RESULT.eq(lotteryVO.getShowResult()))

            .orderBy(LOTTERY_COMMON_CONFIG.ID.desc());

        // 查询开奖时间是当天之后的
        if (lotteryVO.getIsQueryFinish() != null && lotteryVO.getIsQueryFinish()) {
            Date nowBegin = new Date();
            nowBegin.setHours(0);
            nowBegin.setMinutes(0);
            nowBegin.setSeconds(0);
            queryWrapper.and(LOTTERY_COMMON_CONFIG.DRAW_TIME.gt(nowBegin));
        }

        return super.mapper.query(lotteryVO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param lotteryVO
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(LotteryVO lotteryVO) {
        LotteryCommonConfigDomain config = super.mapper.find(lotteryVO.getId());

        if (VerifyUtils.isNotEmpty(lotteryVO.getCoverUrl()) && VerifyUtils.isNotEmpty(config.getCoverUrl())
            && !lotteryVO.getCoverUrl().equals(config.getCoverUrl())) {
            uploadService.updateFileOfficial(lotteryVO.getCoverUrl());
        }

        super.updateById(lotteryVO);

        LotteryTypeService typeService = this.getLotteryTypeService(lotteryVO.getLotteryType());

        Object lotteryVObject = typeService.getLotteryObject(lotteryVO);
        if (lotteryVObject != null) {
            typeService.update(lotteryVObject);
        }

        List<Object> prizes = typeService.getLotteryPrizes(lotteryVObject);
        // 更新和新增奖品，同时收集新奖品ID
        List<Long> newPrizeIds = new ArrayList<>(prizes.size());
        for (Object prize : prizes) {
            typeService.createOrUpdatePrize(lotteryVObject, prize);

            Long prizeId = typeService.getPrizeId(prize);
            if (prizeId != null) {
                newPrizeIds.add(prizeId);
            }
        }

        // 只遍历一次数据库奖品，检查不存在于新奖品中的ID并删除
        List dbPrizes = typeService.queryPrizes(lotteryVO.getId());
        for (Object dbPrize : dbPrizes) {
            Long prizeId = typeService.getPrizeId(dbPrize);
            if (!newPrizeIds.contains(prizeId)) {
                typeService.deletePrize(prizeId);
            }
        }

    }

    /**
     * 删除
     *
     * @param id
     */
    public void delete(Long id) {
        super.mapper.deleteById(id);
    }

    /**
     * 抽奖所有活动
     */
    public void drawAll() {
        // TODO: 解决时间问题，库和代码必须时区相同，要不有问题
        List<LotteryCommonConfigDomain> configs =
            this.queryChain().select(LOTTERY_COMMON_CONFIG.ID, LOTTERY_COMMON_CONFIG.LOTTERY_TYPE)
                .where(LOTTERY_COMMON_CONFIG.DRAW_STATUS.eq(10))
                .and(LOTTERY_COMMON_CONFIG.DRAW_TIME.le(new Date(new Date().getTime() + 8 * 60 * 60 * 1000))).list();

        configs.forEach(config -> draw(config.getId(), config.getLotteryType()));
    }

    /**
     * 抽奖单个活动
     *
     * @param id
     */
    public void drawById(Long id) {
        LotteryCommonConfigDomain config = this.queryChain()
            .select(LOTTERY_COMMON_CONFIG.ID, LOTTERY_COMMON_CONFIG.LOTTERY_TYPE, LOTTERY_COMMON_CONFIG.DRAW_TIME)
            .where(LOTTERY_COMMON_CONFIG.ID.eq(id)).one();

        if (config == null || config.getDrawStatus() != 10 || (config.getDrawTime() != null && config.getDrawTime()
            .before(new Date()))) {
            return;
        }

        draw(config.getId(), config.getLotteryType());
    }

    /**
     * TODO: 事物有问题，字方法报错没回滚
     *
     * @param id
     * @param type
     */
    @Transactional(rollbackFor = Exception.class)
    protected void draw(Long id, Integer type) {
        this.updateChain().set(LOTTERY_COMMON_CONFIG.DRAW_STATUS, 20).where(LOTTERY_COMMON_CONFIG.ID.eq(id)).update();

        LotteryTypeService typeService = this.getLotteryTypeService(type);

        List<LotteryCommonUserDomain> users = lotteryCommonUserService.queryChain().select(LOTTERY_COMMON_USER.ID)
            .where(LOTTERY_COMMON_USER.LOTTERY_ID.eq(id)).list();

        List<Long> userIds = users.stream().map(LotteryCommonUserDomain::getId).collect(Collectors.toList());

        typeService.draw(id, userIds);

        this.updateChain().set(LOTTERY_COMMON_CONFIG.DRAW_STATUS, 30).where(LOTTERY_COMMON_CONFIG.ID.eq(id)).update();
    }

    public void drawNotifyAll() {
        // TODO: 解决时间问题，库和代码必须时区相同，要不有问题
        List<LotteryCommonConfigDomain> configs =
            this.queryChain().select(LOTTERY_COMMON_CONFIG.ID, LOTTERY_COMMON_CONFIG.LOTTERY_TYPE)
                .innerJoin(NOTIFY_CONFIG_WECHAT_APPLET)
                .on(NOTIFY_CONFIG_WECHAT_APPLET.DATA_ID.eq(LOTTERY_COMMON_CONFIG.ID)
                    .and(NOTIFY_CONFIG_WECHAT_APPLET.DATA_TYPE.eq(100))
                    .and(NOTIFY_CONFIG_WECHAT_APPLET.NOTIFIED.eq(false)))
                .where(LOTTERY_COMMON_CONFIG.DRAW_STATUS.eq(30))
                .and(LOTTERY_COMMON_CONFIG.DRAW_TIME.le(new Date(new Date().getTime() + 8 * 60 * 60 * 1000)))
                .and(LOTTERY_COMMON_CONFIG.END_TIME.le(new Date(new Date().getTime() + 8 * 60 * 60 * 1000))).list();

        configs.forEach(config -> drawNotify(config.getId()));
    }

    /**
     * 开奖通知
     * TODO: 优化逻辑，字段改为替换方式，不是写死
     *
     * @param lotteryId
     */
    protected void drawNotify(Long lotteryId) {
        NotifyConfigWechatAppletVO notifyConfigWechatApplet =
            notifyConfigWechatAppletService.findByDataId(lotteryId, 100);

        if (notifyConfigWechatApplet == null || notifyConfigWechatApplet.getNotified()) {
            return;
        }

        // TODO: notified 传 true无效，只能是1
        notifyConfigWechatAppletService.updateChain().set(NOTIFY_CONFIG_WECHAT_APPLET.NOTIFIED, 1)
            .where(NOTIFY_CONFIG_WECHAT_APPLET.ID.eq(notifyConfigWechatApplet.getId())).update();

        ApplicationPlatformKeyDomain key = projectPlatformKeyService.findByPlatform(PlatformEnum.WECHAT_APPLET);
        String accessToken = WechatSDK.MINI_APPLET.accessToken(key.getAppid(), key.getSecret(), "client_credential");

        String token = WechatSDK.utils.extractAccessToken(accessToken);

        HashMap params = new HashMap<>(5);
        params.put("template_id", notifyConfigWechatApplet.getTemplateId());
        params.put("miniprogram_state", WechatSDK.utils.getAppletEnv());

        String url = notifyConfigWechatApplet.getUrl();
        if (url != null) {
            url = url.replace("{{id}}", lotteryId.toString());
            params.put("page", url);
        }

        // 查询抽奖详情
        LotteryCommonConfigDomain lottery = super.mapper.find(lotteryId);

        // 查询用户中奖信息
        LotteryCommonUserVO lotteryCommonUserVO = new LotteryCommonUserVO();
        lotteryCommonUserVO.setLotteryId(lotteryId);
        PageOrList<LotteryCommonUserVO> lotteryUsers = lotteryCommonUserService.query(lotteryCommonUserVO);
        String prizeName = "很遗憾您本次未中奖";

        // 查询奖品信息
        LotteryTypeService typeService = this.getLotteryTypeService(lottery.getLotteryType());
        List<Object> prizes = typeService.queryPrizes(lotteryId);
        Map<Long, Object> prizesMap = new HashMap<>();
        for (Object prize : prizes) {
            prizesMap.put(typeService.getPrizeId(prize), prize);
        }

        for (LotteryCommonUserVO record : lotteryUsers.getRecords()) {
            if (record.getPrizeId() == null) {
                continue;
            }

            Object prize = prizesMap.get(record.getPrizeId());
            if (prize == null) {
                continue;
            }

            if (prize instanceof LotteryParticipatePrizeDomain) {
                prizeName = "恭喜您获得" + ((LotteryParticipatePrizeDomain)prize).getName();
            }

            // 组装data格式
            HashMap<String, Object> data = new HashMap<>();
            data.put("thing10", Map.of("value", lottery.getTitle()));
            data.put("thing4", Map.of("value", prizeName));
            data.put("thing1", Map.of("value", "鲁小班提示您奖品即将过期请尽快兑换"));

            params.put("data", data);

            UserWechatDomain user =
                userWechatService.queryChain().where(USER_WECHAT.USER_ID.eq(record.getUserId())).one();
            params.put("touser", user.getOpenid());

            // TODO: 不计算body服务器上报412
            HashMap headers = new HashMap<>(1);
            String body = new ObjectMapper().valueToTree(params).toString();
            headers.put("Content-Length", String.valueOf(body.getBytes(StandardCharsets.UTF_8).length));

            WechatSDK.MINI_APPLET.sendTemplateMessage(token, params, headers);
        }

    }
}
