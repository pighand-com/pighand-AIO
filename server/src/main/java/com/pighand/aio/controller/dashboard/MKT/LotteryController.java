package com.pighand.aio.controller.dashboard.MKT;

import com.pighand.aio.domain.MKT.LotteryCommonConfigDomain;
import com.pighand.aio.service.MKT.LotteryService;
import com.pighand.aio.vo.MKT.LotteryVO;
import com.pighand.framework.spring.api.annotation.*;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 营销 - 抽奖配置
 *
 * @author wangshuli
 * @createDate 2024-11-28 17:21:35
 */
@RestController(path = "dashboard/lottery", docName = "营销 - 抽奖配置")
public class LotteryController extends BaseController<LotteryService> {

    /**
     * @param mktLotteryCommonConfigVO
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "mktLotteryCommonConfigCreate")
    public Result<LotteryVO> create(
        @Validated({ValidationGroup.Create.class}) @RequestBody LotteryVO mktLotteryCommonConfigVO) {
        mktLotteryCommonConfigVO = super.service.create(mktLotteryCommonConfigVO);

        return new Result(mktLotteryCommonConfigVO);
    }

    /**
     * @param id
     * @return
     */
    @Get(path = "{id}", docSummary = "详情")
    public Result<LotteryCommonConfigDomain> find(@PathVariable(name = "id") Long id) {
        LotteryCommonConfigDomain mktLotteryCommonConfigDomain = super.service.find(id);

        return new Result(mktLotteryCommonConfigDomain);
    }

    /**
     * @param mktLotteryCommonConfigVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "mktLotteryCommonConfigQuery")
    public Result<PageOrList<LotteryVO>> query(LotteryVO mktLotteryCommonConfigVO) {
        PageOrList<LotteryVO> result = super.service.query(mktLotteryCommonConfigVO);

        return new Result(result);
    }

    /**
     * @param mktLotteryCommonConfigVO
     */
    @Put(path = "{id}", docSummary = "修改", fieldGroup = "mktLotteryCommonConfigUpdate")
    public Result update(@PathVariable(name = "id") Long id, @RequestBody LotteryVO mktLotteryCommonConfigVO) {
        mktLotteryCommonConfigVO.setId(id);

        super.service.update(mktLotteryCommonConfigVO);

        return new Result();
    }

    /**
     * @param id
     */
    @Delete(path = "{id}", docSummary = "删除")
    public Result delete(@PathVariable(name = "id") Long id) {
        super.service.delete(id);
        return new Result();
    }
}
