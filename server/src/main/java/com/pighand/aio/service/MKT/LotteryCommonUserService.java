package com.pighand.aio.service.MKT;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.domain.MKT.LotteryCommonConfigDomain;
import com.pighand.aio.domain.MKT.LotteryCommonUserDomain;
import com.pighand.aio.domain.MKT.LotteryParticipatePrizeDomain;
import com.pighand.aio.mapper.MKT.LotteryCommonConfigMapper;
import com.pighand.aio.mapper.MKT.LotteryCommonUserMapper;
import com.pighand.aio.service.MKT.lotteryType.LotteryTypeService;
import com.pighand.aio.vo.MKT.LotteryCommonUserVO;
import com.pighand.aio.vo.MKT.LotteryParticipateVO;
import com.pighand.aio.vo.base.LoginUser;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.pighand.aio.domain.MKT.table.LotteryCommonConfigTableDef.LOTTERY_COMMON_CONFIG;
import static com.pighand.aio.domain.MKT.table.LotteryCommonUserTableDef.LOTTERY_COMMON_USER;
import static com.pighand.aio.domain.base.table.UserExtensionTableDef.USER_EXTENSION;
import static com.pighand.aio.domain.base.table.UserTableDef.USER;

@Service
@RequiredArgsConstructor
public class LotteryCommonUserService extends BaseServiceImpl<LotteryCommonUserMapper, LotteryCommonUserDomain> {

    private final LotteryCommonConfigMapper lotteryCommonConfigMapper;

    private final LotteryTypeService<LotteryParticipateVO, LotteryParticipatePrizeDomain> lotteryTypeParticipateService;

    /**
     * 统计参与人数
     *
     * @param lotteryId
     * @return
     */
    public long count(Long lotteryId) {
        return super.queryChain().select(LOTTERY_COMMON_USER.ID).where(LOTTERY_COMMON_USER.LOTTERY_ID.eq(lotteryId))
            .count();
    }

    /**
     * 判断用户是否已参加抽奖
     *
     * @param lotteryId
     * @return null: 未参加
     */
    public LotteryCommonUserDomain isParticipate(Long lotteryId) {
        LoginUser loginUser = Context.loginUser();
        if (loginUser == null || loginUser.getId() == null) {
            return null;
        }

        LotteryCommonUserDomain LotteryCommonUser =
            super.queryChain().select(LOTTERY_COMMON_USER.ID, LOTTERY_COMMON_USER.PRIZE_ID).where(
                    LOTTERY_COMMON_USER.LOTTERY_ID.eq(lotteryId).and(LOTTERY_COMMON_USER.USER_ID.eq(loginUser.getId())))
                .one();

        return LotteryCommonUser;
    }

    /**
     * 参加抽奖
     *
     * @param lotteryId
     */
    public void participate(Long lotteryId) {
        LotteryCommonUserDomain joinLotteryCommonUserDomain = this.isParticipate(lotteryId);
        if (joinLotteryCommonUserDomain != null) {
            throw new RuntimeException("您已参加抽奖");
        }

        LotteryCommonUserDomain lotteryCommonUser = new LotteryCommonUserDomain();
        lotteryCommonUser.setLotteryId(lotteryId);
        lotteryCommonUser.setUserId(Context.loginUser().getId());
        lotteryCommonUser.setCreatedAt(new Date());
        super.save(lotteryCommonUser);
    }

    private LotteryTypeService getLotteryTypeService(Integer type) {
        return switch (type) {
            case 10 -> lotteryTypeParticipateService;
            default -> throw new RuntimeException("不支持的抽奖类型: " + type);
        };
    }

    /**
     * 查询抽奖参与用户
     *
     * @param lotteryCommonUserVO
     * @return
     */
    public PageOrList<LotteryCommonUserVO> query(LotteryCommonUserVO lotteryCommonUserVO) {
        LotteryCommonConfigDomain lotteryCommonConfig = lotteryCommonConfigMapper.selectOneByQuery(
            QueryWrapper.create().select(LOTTERY_COMMON_CONFIG.LOTTERY_TYPE)
                .where(LOTTERY_COMMON_CONFIG.ID.eq(lotteryCommonUserVO.getLotteryId())));

        if (lotteryCommonConfig == null) {
            throw new RuntimeException("抽奖不存在");
        }

        lotteryCommonUserVO.setJoinTables(USER.getTableName(), USER_EXTENSION.getTableName());

        QueryWrapper queryWrapper = QueryWrapper.create().select(LOTTERY_COMMON_USER.DEFAULT_COLUMNS)

            // equal
            .and(LOTTERY_COMMON_USER.LOTTERY_ID.eq(lotteryCommonUserVO.getLotteryId()));

        if (lotteryCommonUserVO.getIsOrderPrize() != null && lotteryCommonUserVO.getIsOrderPrize()) {
            queryWrapper.orderBy(
                LOTTERY_COMMON_USER.PRIZE_ID.getTable().getName() + "." + LOTTERY_COMMON_USER.PRIZE_ID.getName()
                    + " is null").orderBy(LOTTERY_COMMON_USER.PRIZE_ID.asc());
        }

        PageOrList<LotteryCommonUserVO> result = super.mapper.query(lotteryCommonUserVO, queryWrapper);

        // 查询抽奖的奖品信息
        List<Long> prizeIds = result.getRecords().stream().map(LotteryCommonUserVO::getPrizeId).filter(Objects::nonNull)
            .collect(Collectors.toList());

        LotteryTypeService typeService = this.getLotteryTypeService(lotteryCommonConfig.getLotteryType());
        List<LotteryParticipatePrizeDomain> prizes = typeService.queryPrizes(prizeIds);

        Map<Long, LotteryParticipatePrizeDomain> prizeMap =
            prizes.stream().collect(Collectors.toMap(LotteryParticipatePrizeDomain::getId, prize -> prize));

        // 回填奖品信息
        result.getRecords().forEach(user -> {
            if (user.getPrizeId() != null) {
                user.setPrize(prizeMap.get(user.getPrizeId()));
            }
        });

        return result;
    }
}
