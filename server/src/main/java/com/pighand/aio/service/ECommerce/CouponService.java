package com.pighand.aio.service.ECommerce;

import com.pighand.aio.domain.ECommerce.CouponDomain;
import com.pighand.aio.vo.ECommerce.CouponVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;

/**
 * 电商 - 优惠券
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
public interface CouponService extends BaseService<CouponDomain> {

    /**
     * 创建
     *
     * @param couponVO
     * @return
     */
    CouponVO create(CouponVO couponVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    CouponDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param couponVO
     * @return PageOrList<CouponVO>
     */
    PageOrList<CouponVO> query(CouponVO couponVO);

    /**
     * 修改
     *
     * @param couponVO
     */
    void update(CouponVO couponVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}
