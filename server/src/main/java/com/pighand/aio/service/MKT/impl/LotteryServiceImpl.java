package com.pighand.aio.service.MKT.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.MKT.LotteryCommonConfigDomain;
import com.pighand.aio.domain.MKT.LotteryCommonUserDomain;
import com.pighand.aio.domain.MKT.LotteryParticipatePrizeDomain;
import com.pighand.aio.mapper.MKT.LotteryCommonConfigMapper;
import com.pighand.aio.service.MKT.LotteryCommonUserService;
import com.pighand.aio.service.MKT.LotteryService;
import com.pighand.aio.service.MKT.lotteryType.LotteryTypeService;
import com.pighand.aio.service.common.UploadService;
import com.pighand.aio.vo.MKT.LotteryParticipateVO;
import com.pighand.aio.vo.MKT.LotteryVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.pighand.aio.domain.MKT.table.LotteryCommonConfigTableDef.LOTTERY_COMMON_CONFIG;
import static com.pighand.aio.domain.MKT.table.LotteryCommonUserTableDef.LOTTERY_COMMON_USER;

/**
 * 营销 - 抽奖配置
 *
 * @author wangshuli
 * @createDate 2024-11-28 17:21:35
 */
@Service
@RequiredArgsConstructor
public class LotteryServiceImpl extends BaseServiceImpl<LotteryCommonConfigMapper, LotteryCommonConfigDomain>
    implements LotteryService {

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
     * @param lotteryO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public LotteryVO create(LotteryVO lotteryO) {
        lotteryO.setDrawStatus(10);
        super.mapper.insert(lotteryO);

        LotteryTypeService typeService = this.getLotteryTypeService(lotteryO.getLotteryType());

        Object lotteryObject = typeService.getLotteryObject(lotteryO);
        List<Object> prizes = typeService.getLotteryPrizes(lotteryObject);

        typeService.create(lotteryO.getId(), lotteryObject);
        typeService.createPrizes(lotteryO.getId(), prizes);

        uploadService.updateFileOfficial(lotteryO.getCoverUrl());

        return lotteryO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public LotteryVO find(Long id) {
        LotteryVO lotteryVO = super.mapper.find(id);

        LotteryTypeService typeService = this.getLotteryTypeService(lotteryVO.getLotteryType());

        Object lotteryObject = typeService.find(id);
        List<Object> prizes = typeService.queryPrizes(id);

        typeService.setLotteryPrizes(lotteryObject, prizes);
        typeService.setLotteryObject(lotteryVO, lotteryObject);

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
        lotteryVO.setIsBegin(lotteryVO.getBeginTime().before(now));
        lotteryVO.setIsEnd(lotteryVO.getEndTime().before(now));

        return lotteryVO;
    }

    /**
     * 分页或列表
     *
     * @param lotteryO
     * @return PageOrList<lotteryO>
     */
    @Override
    public PageOrList<LotteryVO> query(LotteryVO lotteryO) {

        QueryWrapper queryWrapper = QueryWrapper.create()

            // like
            .and(LOTTERY_COMMON_CONFIG.LOTTERY_TYPE.like(lotteryO.getLotteryType()))
            .and(LOTTERY_COMMON_CONFIG.TITLE.like(lotteryO.getTitle()))
            .and(LOTTERY_COMMON_CONFIG.SHARE_IMAGE_URL.like(lotteryO.getShareImageUrl()))
            .and(LOTTERY_COMMON_CONFIG.HELP_POSTER_URL.like(lotteryO.getHelpPosterUrl()))
            .and(LOTTERY_COMMON_CONFIG.MORE_BUTTON_URL.like(lotteryO.getMoreButtonUrl()))
            .and(LOTTERY_COMMON_CONFIG.MORE_EXTERNAL_URL.like(lotteryO.getMoreExternalUrl()))
            .and(LOTTERY_COMMON_CONFIG.RESULT_NOTIFY_URL.like(lotteryO.getResultNotifyUrl()))

            // equal
            .and(LOTTERY_COMMON_CONFIG.HELP_ADD_BY_TIMES.eq(lotteryO.getHelpAddByTimes()))
            .and(LOTTERY_COMMON_CONFIG.MAX_HELP_ME.eq(lotteryO.getMaxHelpMe()))
            .and(LOTTERY_COMMON_CONFIG.MAX_HELP_OTHER.eq(lotteryO.getMaxHelpOther()))
            .and(LOTTERY_COMMON_CONFIG.SHOW_RESULT.eq(lotteryO.getShowResult()))

            .orderBy(LOTTERY_COMMON_CONFIG.ID.desc());

        // 查询开奖时间是当天之后的
        if (true == lotteryO.getIsQueryFinish()) {
            Date nowBegin = new Date();
            nowBegin.setHours(0);
            nowBegin.setMinutes(0);
            nowBegin.setSeconds(0);
            queryWrapper.and(LOTTERY_COMMON_CONFIG.DRAW_TIME.gt(nowBegin));
        }

        return super.mapper.query(lotteryO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param lotteryO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(LotteryVO lotteryO) {
        LotteryCommonConfigDomain config = super.mapper.find(lotteryO.getId());

        if (VerifyUtils.isNotEmpty(lotteryO.getCoverUrl()) && VerifyUtils.isNotEmpty(config.getCoverUrl())
            && !lotteryO.getCoverUrl().equals(config.getCoverUrl())) {
            uploadService.updateFileOfficial(lotteryO.getCoverUrl());
        }

        super.updateById(lotteryO);

        LotteryTypeService typeService = this.getLotteryTypeService(lotteryO.getLotteryType());

        Object lotteryObject = typeService.getLotteryObject(lotteryO);
        if (lotteryObject != null) {
            typeService.update(lotteryObject);
        }

        List<Object> prizes = typeService.getLotteryPrizes(lotteryObject);
        // 更新和新增奖品，同时收集新奖品ID
        List<Long> newPrizeIds = new ArrayList<>(prizes.size());
        for (Object prize : prizes) {
            typeService.createOrUpdatePrize(lotteryObject, prize);

            Long prizeId = typeService.getPrizeId(prize);
            if (prizeId != null) {
                newPrizeIds.add(prizeId);
            }
        }

        // 只遍历一次数据库奖品，检查不存在于新奖品中的ID并删除
        List dbPrizes = typeService.queryPrizes(lotteryO.getId());
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
    @Override
    public void delete(Long id) {
        super.mapper.deleteById(id);
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

    @Override
    public void drawAll() {
        List<LotteryCommonConfigDomain> configs =
            this.queryChain().select(LOTTERY_COMMON_CONFIG.ID, LOTTERY_COMMON_CONFIG.LOTTERY_TYPE)
                .where(LOTTERY_COMMON_CONFIG.DRAW_STATUS.eq(10)).and(LOTTERY_COMMON_CONFIG.DRAW_TIME.le(new Date()))
                .list();

        configs.forEach(config -> draw(config.getId(), config.getLotteryType()));
    }

    /**
     * 抽奖
     *
     * @param id
     */
    @Override
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
}
