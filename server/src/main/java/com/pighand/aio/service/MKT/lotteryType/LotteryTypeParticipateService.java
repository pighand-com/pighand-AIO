package com.pighand.aio.service.MKT.lotteryType;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.MKT.LotteryParticipateDomain;
import com.pighand.aio.domain.MKT.LotteryParticipatePrizeDomain;
import com.pighand.aio.mapper.MKT.LotteryParticipateMapper;
import com.pighand.aio.mapper.MKT.LotteryParticipatePrizeMapper;
import com.pighand.aio.vo.MKT.LotteryParticipateVO;
import com.pighand.aio.vo.MKT.LotteryVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.pighand.aio.domain.MKT.table.LotteryParticipatePrizeTableDef.LOTTERY_PARTICIPATE_PRIZE;

@Service
@RequiredArgsConstructor
public class LotteryTypeParticipateService extends BaseServiceImpl<LotteryParticipateMapper, LotteryParticipateDomain>
    implements LotteryTypeService<LotteryParticipateVO, LotteryParticipatePrizeDomain> {

    private final LotteryParticipatePrizeMapper lotteryParticipatePrizeMapper;

    @Override
    public LotteryParticipateVO getLotteryObject(LotteryVO lotteryVO) {
        return lotteryVO.getParticipate();
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
        super.mapper.update(lotteryObject);
    }

    @Override
    public void createPrizes(List<LotteryParticipatePrizeDomain> prizes) {
        lotteryParticipatePrizeMapper.insertBatch(prizes);
    }

    @Override
    public List<LotteryParticipatePrizeDomain> queryPrizes(Long lotteryId) {
        QueryWrapper queryWrapper =
            QueryWrapper.create().where(LOTTERY_PARTICIPATE_PRIZE.LOTTERY_PARTICIPATE_ID.eq(lotteryId));

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

}
