package com.pighand.aio.service.MKT.impl;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.update.UpdateChain;
import com.pighand.aio.domain.MKT.LotteryCommonConfigDomain;
import com.pighand.aio.domain.MKT.LotteryParticipatePrizeDomain;
import com.pighand.aio.mapper.MKT.LotteryCommonConfigMapper;
import com.pighand.aio.service.MKT.LotteryService;
import com.pighand.aio.service.MKT.lotteryType.LotteryTypeService;
import com.pighand.aio.vo.MKT.LotteryParticipateVO;
import com.pighand.aio.vo.MKT.LotteryVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.VerifyUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.pighand.aio.domain.MKT.table.LotteryCommonConfigTableDef.LOTTERY_COMMON_CONFIG;

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

    private final LotteryTypeService<LotteryParticipateVO, LotteryParticipatePrizeDomain> lotteryTypeParticipateService;

    private LotteryTypeService getLotteryTypeService(String type) {
        return switch (type) {
            case "participate" -> lotteryTypeParticipateService;
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
        super.mapper.insert(lotteryO);

        LotteryTypeService typeService = this.getLotteryTypeService(lotteryO.getLotteryType());

        Object lotteryObject = typeService.getLotteryObject(lotteryO);
        List<Object> prizes = typeService.getLotteryPrizes(lotteryObject);

        typeService.create(lotteryO.getId(), lotteryObject);
        typeService.createPrizes(prizes);

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
        LotteryVO lotteryO = super.mapper.find(id);

        LotteryTypeService typeService = this.getLotteryTypeService(lotteryO.getLotteryType());

        Object lotteryObject = typeService.find(id);
        List<Object> prizes = typeService.queryPrizes(id);

        typeService.setLotteryPrizes(lotteryObject, prizes);
        typeService.setLotteryObject(lotteryO, lotteryObject);

        return lotteryO;
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
            .and(LOTTERY_COMMON_CONFIG.SHARE_TITLE.like(lotteryO.getShareTitle()))
            .and(LOTTERY_COMMON_CONFIG.SHARE_IMAGE_URL.like(lotteryO.getShareImageUrl()))
            .and(LOTTERY_COMMON_CONFIG.HELP_POSTER_URL.like(lotteryO.getHelpPosterUrl()))
            .and(LOTTERY_COMMON_CONFIG.MORE_BUTTON_URL.like(lotteryO.getMoreButtonUrl()))
            .and(LOTTERY_COMMON_CONFIG.MORE_EXTERNAL_URL.like(lotteryO.getMoreExternalUrl()))
            .and(LOTTERY_COMMON_CONFIG.RESULT_NOTIFY_URL.like(lotteryO.getResultNotifyUrl()))

            // equal
            .and(LOTTERY_COMMON_CONFIG.HELP_ADD_BY_TIMES.eq(lotteryO.getHelpAddByTimes()))
            .and(LOTTERY_COMMON_CONFIG.MAX_HELP_ME.eq(lotteryO.getMaxHelpMe()))
            .and(LOTTERY_COMMON_CONFIG.MAX_HELP_OTHER.eq(lotteryO.getMaxHelpOther()))
            .and(LOTTERY_COMMON_CONFIG.SHOW_RESULT.eq(lotteryO.getShowResult()));

        return super.mapper.query(lotteryO, queryWrapper);
    }

    /**
     * 修改
     *
     * @param lotteryO
     */
    @Override
    public void update(LotteryVO lotteryO) {
        UpdateChain updateChain = this.updateChain().where(LOTTERY_COMMON_CONFIG.ID.eq(lotteryO.getId()));
        updateChain.set(LOTTERY_COMMON_CONFIG.ID, lotteryO.getId(), VerifyUtils::isNotEmpty);

        updateChain.update();

        LotteryTypeService typeService = this.getLotteryTypeService(lotteryO.getLotteryType());

        Object lotteryObject = typeService.find(lotteryO.getId());
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
}
