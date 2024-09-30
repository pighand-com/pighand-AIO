package com.pighand.aio.service.ECommerce;

import com.pighand.aio.domain.ECommerce.CouponUserDomain;
import com.pighand.aio.vo.ECommerce.CouponUserVO;
import com.pighand.framework.spring.base.BaseService;
import com.pighand.framework.spring.page.PageOrList;
import org.springframework.transaction.annotation.Transactional;

/**
 * 电商 - 优惠券 - 用户已领
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
public interface CouponUserService extends BaseService<CouponUserDomain> {

    /**
     * 创建
     *
     * @param couponUserVO
     * @return
     */
    CouponUserVO create(CouponUserVO couponUserVO);

    /**
     * 查询已领优惠券
     *
     * @param couponUserVO
     * @return PageOrList<CouponUserVO>
     */
    PageOrList<CouponUserVO> query(CouponUserVO couponUserVO);

    /**
     * 转赠
     *
     * @param couponId
     * @param toUserId
     */
    @Transactional(rollbackFor = Exception.class)
    void transfer(Long couponId, Long toUserId);

    /**
     * 核销
     */
    void verify(Long id, Long creatorId);

    /**
     * 查询核销记录
     *
     * @param couponUserVO
     * @return
     */
    PageOrList<CouponUserVO> queryVerify(CouponUserVO couponUserVO);
}
