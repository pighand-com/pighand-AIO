package com.pighand.aio.controller.client.MKT;

import com.pighand.aio.domain.MKT.LotteryCommonConfigDomain;
import com.pighand.aio.service.MKT.LotteryCommonUserService;
import com.pighand.aio.service.MKT.LotteryService;
import com.pighand.aio.vo.MKT.LotteryCommonUserVO;
import com.pighand.aio.vo.MKT.LotteryVO;
import com.pighand.framework.spring.api.annotation.Get;
import com.pighand.framework.spring.api.annotation.Post;
import com.pighand.framework.spring.api.annotation.RestController;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 营销 - 抽奖配置
 *
 * @author wangshuli
 * @createDate 2024-11-28 17:21:35
 */
@RestController(path = "client/lottery", docName = "营销 - 抽奖配置")
@RequiredArgsConstructor
public class LotteryController extends BaseController<LotteryService> {

    private final LotteryCommonUserService lotteryCommonUserService;

    @Post(docSummary = "参与", value = "{id}")
    public Result create(@PathVariable(name = "id") Long id) {
        lotteryCommonUserService.participate(id);

        return new Result();
    }

    @Get(docSummary = "列表")
    public Result<PageOrList<LotteryVO>> query(LotteryVO mktLotteryCommonConfigVO) {
        mktLotteryCommonConfigVO.setIsQueryFinish(true);
        PageOrList<LotteryVO> result = super.service.query(mktLotteryCommonConfigVO);

        return new Result(result);
    }

    @Get(path = "{id}", docSummary = "详情")
    public Result<LotteryCommonConfigDomain> find(@PathVariable(name = "id") Long id) {
        LotteryCommonConfigDomain mktLotteryCommonConfigDomain = super.service.find(id);

        return new Result(mktLotteryCommonConfigDomain);
    }

    @Get(docSummary = "查询参与用户", value = "{id}/participate")
    public Result<PageOrList<LotteryCommonUserVO>> queryParticipate(@PathVariable(name = "id") Long id,
        LotteryCommonUserVO mktLotteryCommonUserVO) {
        mktLotteryCommonUserVO.setLotteryId(id);
        PageOrList<LotteryCommonUserVO> result = lotteryCommonUserService.query(mktLotteryCommonUserVO);

        return new Result(result);
    }
}
