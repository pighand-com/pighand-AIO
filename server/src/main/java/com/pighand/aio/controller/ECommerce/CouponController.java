package com.pighand.aio.controller.ECommerce;

import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.domain.ECommerce.CouponDomain;
import com.pighand.aio.service.ECommerce.CouponService;
import com.pighand.aio.service.ECommerce.impl.CouponUserServiceImpl;
import com.pighand.aio.vo.ECommerce.CouponUserTransferVO;
import com.pighand.aio.vo.ECommerce.CouponUserVO;
import com.pighand.aio.vo.ECommerce.CouponVO;
import com.pighand.framework.spring.api.annotation.Get;
import com.pighand.framework.spring.api.annotation.Post;
import com.pighand.framework.spring.api.annotation.Put;
import com.pighand.framework.spring.api.annotation.RestController;
import com.pighand.framework.spring.api.annotation.validation.ValidationGroup;
import com.pighand.framework.spring.base.BaseController;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.response.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * 电商 - 优惠券
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@RequiredArgsConstructor
@RestController(path = "coupon", docName = "电商 - 优惠券")
public class CouponController extends BaseController<CouponService> {

    private final CouponUserServiceImpl couponUserService;

    @Post(docSummary = "创建", fieldGroup = "couponCreate")
    public Result<CouponVO> create(@Validated({ValidationGroup.Create.class}) @RequestBody CouponVO couponVO) {
        couponVO = super.service.create(couponVO);

        return new Result(couponVO);
    }

    @Get(path = "{id}", docSummary = "详情")
    public Result<CouponDomain> find(@PathVariable(name = "id") Long id) {
        CouponDomain couponDomain = super.service.find(id);

        return new Result(couponDomain);
    }

    @Get(docSummary = "分页或列表", fieldGroup = "couponQuery")
    public Result<PageOrList<CouponVO>> query(CouponVO couponVO) {
        PageOrList<CouponVO> result = super.service.query(couponVO);

        return new Result(result);
    }

    @Put(path = "{id}", docSummary = "修改", fieldGroup = "couponUpdate")
    public Result update(@PathVariable(name = "id") Long id, @RequestBody CouponVO couponVO) {
        couponVO.setId(id);
        super.service.update(couponVO);

        return new Result();
    }

    /**
     * @param id
     */
    @com.pighand.framework.spring.api.annotation.Delete(path = "{id}", docSummary = "删除")
    public Result delete(@PathVariable(name = "id") Long id) {
        super.service.delete(id);
        return new Result();
    }

    @Post(path = "send", docSummary = "发券")
    public Result<CouponVO> send(CouponUserVO couponUserVO) {
        couponUserService.create(couponUserVO);

        return new Result();
    }

    @Get(path = "verify", docSummary = "查询核销记录")
    public Result<PageOrList<CouponUserVO>> queryVerify(CouponUserVO couponUserVO) {
        PageOrList<CouponUserVO> result = couponUserService.queryVerify(couponUserVO);

        return new Result(result);
    }

    @Post(path = "verify/{id}", docSummary = "核销")
    public Result<CouponVO> verify(@PathVariable(name = "id") Long id, @RequestBody CouponUserVO couponUserVO) {
        couponUserService.verify(id, couponUserVO.getCreatorId());

        return new Result();
    }

    @Get(path = "mine", docSummary = "我的")
    public Result<PageOrList<CouponVO>> queryMine(CouponUserVO couponUserVO) {
        couponUserVO.setCreatorId(Context.getLoginUser().getId());
        PageOrList<CouponUserVO> result = couponUserService.query(couponUserVO);

        return new Result(result);
    }

    @Put(path = "transfer/{id}", docSummary = "转增")
    public Result<CouponVO> transfer(@PathVariable(name = "id") Long id,
        @RequestBody CouponUserTransferVO couponUserTransferVO) {
        couponUserService.transfer(id, couponUserTransferVO.getToUserId());

        return new Result();
    }
}
