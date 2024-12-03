package com.pighand.aio.service.MKT;

import com.pighand.aio.domain.MKT.LotteryCommonConfigDomain;
import com.pighand.aio.vo.MKT.LotteryVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;

/**
 * 营销 - 抽奖配置
 *
 * @author wangshuli
 * @createDate 2024-11-28 17:21:35
 */
public interface LotteryService extends BaseService<LotteryCommonConfigDomain> {

    /**
     * 创建
     *
     * @param mktLotteryCommonConfigVO
     * @return
     */
    LotteryVO create(LotteryVO mktLotteryCommonConfigVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    LotteryVO find(Long id);

    /**
     * 分页或列表
     *
     * @param mktLotteryCommonConfigVO
     * @return PageOrList<MktLotteryCommonConfigVO>
     */
    PageOrList<LotteryVO> query(LotteryVO mktLotteryCommonConfigVO);

    /**
     * 修改
     *
     * @param mktLotteryCommonConfigVO
     */
    void update(LotteryVO mktLotteryCommonConfigVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}
