package com.pighand.aio.controller.dashboard.distribution;

import com.pighand.aio.domain.distribution.DistributionGoodsRuleDomain;
import com.pighand.aio.service.distribution.DistributionGoodsRuleService;
import com.pighand.aio.vo.distribution.DistributionGoodsRuleVO;
import com.pighand.framework.spring.api.annotation.*;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import com.pighand.framework.spring.util.VerifyUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 分销 - 分销商品规则
 *
 * @author wangshuli
 * @createDate 2025-05-24 14:35:05
 */
@RestController(path = "dashboard/distribution/goods/rule", docName = "分销 - 分销商品规则")
public class DistributionGoodsRuleController extends BaseController<DistributionGoodsRuleService> {

    /**
     * 重置分成金额
     *
     * @param distDistributionGoodsRuleVO
     */
    private void resetSharing(DistributionGoodsRuleVO distDistributionGoodsRuleVO) {
        Integer sharingType = distDistributionGoodsRuleVO.getSharingType();
        if (VerifyUtils.isNotEmpty(sharingType)) {
            if (distDistributionGoodsRuleVO.getSharingType().equals(10)) {
                distDistributionGoodsRuleVO.setSharingPrice(null);
            } else if (distDistributionGoodsRuleVO.getSharingType().equals(20)) {
                distDistributionGoodsRuleVO.setSharingRatio(null);
            }
        }
    }

    /**
     * @param distDistributionGoodsRuleVO
     * @return
     */
    @Post(docSummary = "创建", fieldGroup = "distDistributionGoodsRuleCreate")
    public Result<DistributionGoodsRuleVO> create(
        @Validated({ValidationGroup.Create.class}) @RequestBody DistributionGoodsRuleVO distDistributionGoodsRuleVO) {

        this.resetSharing(distDistributionGoodsRuleVO);

        distDistributionGoodsRuleVO = super.service.create(distDistributionGoodsRuleVO);

        return new Result(distDistributionGoodsRuleVO);
    }

    /**
     * @param id
     * @return
     */
    @Get(path = "{id}", docSummary = "详情")
    public Result<DistributionGoodsRuleDomain> find(@PathVariable(name = "id") Long id) {
        DistributionGoodsRuleDomain distDistributionGoodsRuleDomain = super.service.find(id);

        return new Result(distDistributionGoodsRuleDomain);
    }

    /**
     * @param distDistributionGoodsRuleVO
     */
    @Get(docSummary = "分页或列表", fieldGroup = "distDistributionGoodsRuleQuery")
    public Result<PageOrList<DistributionGoodsRuleVO>> query(DistributionGoodsRuleVO distDistributionGoodsRuleVO) {
        PageOrList<DistributionGoodsRuleVO> result = super.service.query(distDistributionGoodsRuleVO);

        return new Result(result);
    }

    /**
     * @param distDistributionGoodsRuleVO
     */
    @Put(path = "{id}", docSummary = "修改", fieldGroup = "distDistributionGoodsRuleUpdate")
    public Result update(@PathVariable(name = "id") Long id,
        @RequestBody DistributionGoodsRuleVO distDistributionGoodsRuleVO) {
        distDistributionGoodsRuleVO.setId(id);

        this.resetSharing(distDistributionGoodsRuleVO);

        super.service.update(distDistributionGoodsRuleVO);

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
