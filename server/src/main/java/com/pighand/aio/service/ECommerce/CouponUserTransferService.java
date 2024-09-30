package com.pighand.aio.service.ECommerce;

import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.aio.domain.ECommerce.CouponUserTransferDomain;
import com.pighand.aio.vo.ECommerce.CouponUserTransferVO;

/**
 * 电商 - 优惠券 - 转赠记录
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
public interface CouponUserTransferService extends BaseService<CouponUserTransferDomain> {

    /**
     * 创建
     *
     * @param couponUserTransferVO
     * @return
     */
    CouponUserTransferVO create(CouponUserTransferVO couponUserTransferVO);

    /**
     * 详情
     *
     * @param id
     * @return
     */
    CouponUserTransferDomain find(Long id);

    /**
     * 分页或列表
     *
     * @param couponUserTransferVO
     * @return PageOrList<CouponUserTransferVO>
     */
    PageOrList<CouponUserTransferVO> query(CouponUserTransferVO couponUserTransferVO);

    /**
     * 修改
     *
     * @param couponUserTransferVO
     */
    void update(CouponUserTransferVO couponUserTransferVO);

    /**
     * 删除
     *
     * @param id
     */
    void delete(Long id);
}
