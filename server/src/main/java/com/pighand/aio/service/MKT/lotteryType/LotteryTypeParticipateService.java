package com.pighand.aio.service.MKT.lotteryType;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.MKT.LotteryCommonUserDomain;
import com.pighand.aio.domain.MKT.LotteryParticipateDomain;
import com.pighand.aio.domain.MKT.LotteryParticipatePrizeDomain;
import com.pighand.aio.mapper.MKT.LotteryCommonUserMapper;
import com.pighand.aio.mapper.MKT.LotteryParticipateMapper;
import com.pighand.aio.mapper.MKT.LotteryParticipatePrizeMapper;
import com.pighand.aio.vo.MKT.LotteryParticipateVO;
import com.pighand.aio.vo.MKT.LotteryVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.pighand.aio.domain.MKT.table.LotteryParticipatePrizeTableDef.LOTTERY_PARTICIPATE_PRIZE;

@Service
@RequiredArgsConstructor
public class LotteryTypeParticipateService extends BaseServiceImpl<LotteryParticipateMapper, LotteryParticipateDomain>
    implements LotteryTypeService<LotteryParticipateVO, LotteryParticipatePrizeDomain> {

    private final LotteryParticipatePrizeMapper lotteryParticipatePrizeMapper;

    private final LotteryCommonUserMapper lotteryCommonUserMapper;

    @Override
    public LotteryParticipateVO getLotteryObject(LotteryVO lotteryVO) {
        return Optional.ofNullable(lotteryVO.getParticipate()).orElse(new LotteryParticipateVO());
    }

    @Override
    public void setLotteryObject(LotteryVO lotteryVO, LotteryParticipateVO lotteryObject) {
        lotteryVO.setParticipate(lotteryObject);
    }

    @Override
    public List<LotteryParticipatePrizeDomain> getLotteryPrizes(LotteryParticipateVO lotteryObject) {
        return lotteryObject.getPrizes();
    }

    @Override
    public void setLotteryPrizes(LotteryParticipateVO lotteryObject,
        List<LotteryParticipatePrizeDomain> lotteryPrizes) {
        lotteryObject.setPrizes(lotteryPrizes);
    }

    @Override
    public Long getPrizeId(LotteryParticipatePrizeDomain prize) {
        return prize.getId();
    }

    @Override
    public void create(Long lotteryId, LotteryParticipateVO lotteryObject) {
        lotteryObject.setId(lotteryId);
        super.mapper.insert(lotteryObject);
    }

    @Override
    public LotteryParticipateVO find(Long lotteryId) {
        LotteryParticipateVO participate = super.mapper.find(lotteryId);

        return participate;
    }

    @Override
    public void update(LotteryParticipateVO lotteryObject) {
        super.updateById(lotteryObject, false);
    }

    @Override
    public void createPrizes(Long lotteryId, List<LotteryParticipatePrizeDomain> prizes) {
        prizes.forEach(prize -> prize.setLotteryParticipateId(lotteryId));
        lotteryParticipatePrizeMapper.insertBatch(prizes);
    }

    @Override
    public List<LotteryParticipatePrizeDomain> queryPrizes(Long lotteryId) {
        QueryWrapper queryWrapper =
            QueryWrapper.create().where(LOTTERY_PARTICIPATE_PRIZE.LOTTERY_PARTICIPATE_ID.eq(lotteryId));

        return lotteryParticipatePrizeMapper.selectListByQuery(queryWrapper);
    }

    @Override
    public List<LotteryParticipatePrizeDomain> queryPrizes(List<Long> ids) {
        if (ids.isEmpty()) {
            return new ArrayList<>(0);
        }

        QueryWrapper queryWrapper = QueryWrapper.create().where(LOTTERY_PARTICIPATE_PRIZE.ID.in(ids));

        return lotteryParticipatePrizeMapper.selectListByQuery(queryWrapper);
    }

    @Override
    public void createOrUpdatePrize(LotteryParticipateVO lotteryObject, LotteryParticipatePrizeDomain prize) {
        if (prize.getId() != null) {
            lotteryParticipatePrizeMapper.update(prize);
        } else {
            prize.setLotteryParticipateId(lotteryObject.getId());
            lotteryParticipatePrizeMapper.insert(prize);
        }
    }

    @Override
    public void deletePrize(Long prizeId) {
        lotteryParticipatePrizeMapper.deleteById(prizeId);
    }

    /**
     * 随机从用户列表中抽取用户，并根据奖品配额分配奖品，返回中奖用户与奖品的对应关系。
     *
     * <p>逻辑说明：
     * <ul>
     *   <li>1. 根据 lotteryId 查询所有奖品及各自的配额。</li>
     *   <li>2. 计算所有奖品的总配额 totalQuota。</li>
     *   <li>3. 从 allUserIds 中随机抽取 totalQuota 个用户，如果用户数量不足，则全部抽取。</li>
     *   <li>4. 按照奖品的配额顺序，依次为用户分配奖品。</li>
     *   <li>5. 最终返回一个 Map，key 为 userId，value 为对应的奖品 ID。</li>
     * </ul>
     *
     * @param lotteryId      抽奖活动的 ID，用于查询对应奖品信息
     * @param lotteryUserIds 所有参与抽奖的用户列表
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void draw(Long lotteryId, List<Long> lotteryUserIds) {
        List<LotteryParticipatePrizeDomain> prizes = this.queryPrizes(lotteryId);

        // 奖品总配额
        int totalQuota = prizes.stream().mapToInt(LotteryParticipatePrizeDomain::getLotteryQuota).sum();

        // 实际可抽奖用户数
        int actualCount = Math.min(totalQuota, lotteryUserIds.size());

        // 随机抽取 actualCount 个用户
        List<Long> userPool = new ArrayList<>(lotteryUserIds);
        Collections.shuffle(userPool);
        List<Long> selectedUserIds = userPool.subList(0, actualCount);

        int userIndex = 0;

        for (LotteryParticipatePrizeDomain prize : prizes) {
            int quota = prize.getLotteryQuota();
            for (int i = 0; i < quota && userIndex < actualCount; i++) {
                Long userId = selectedUserIds.get(userIndex++);

                LotteryCommonUserDomain user = new LotteryCommonUserDomain();
                user.setId(userId);
                user.setPrizeId(prize.getId());
                lotteryCommonUserMapper.update(user);
            }

            if (userIndex >= actualCount) {
                break; // 所有可分配用户已分完
            }
        }
    }

}
