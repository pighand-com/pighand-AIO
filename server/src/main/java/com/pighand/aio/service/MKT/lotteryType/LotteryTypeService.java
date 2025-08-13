package com.pighand.aio.service.MKT.lotteryType;

import com.pighand.aio.vo.MKT.LotteryVO;

import java.util.List;

public interface LotteryTypeService<D, T> {

    D getLotteryObject(LotteryVO lotteryVO);

    void setLotteryObject(LotteryVO lotteryVO, D lotteryObject);

    List<T> getLotteryPrizes(D lotteryObject);

    void setLotteryPrizes(D lotteryObject, List<T> lotteryPrizes);

    Long getPrizeId(T prize);

    void create(Long lotteryId, D lotteryObject);

    D find(Long lotteryId);

    void update(D lotteryObject);

    void createPrizes(Long lotteryId, List<T> prizes);

    List<T> queryPrizes(Long lotteryId);

    List<T> queryPrizes(List<Long> ids);

    void createOrUpdatePrize(D lotteryObject, T prize);

    void deletePrize(Long prizeId);

    void draw(Long lotteryId, List<Long> allUserIds);
}
