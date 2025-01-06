package com.pighand.aio.service.ECommerce.impl;

import com.pighand.aio.domain.ECommerce.CouponDomain;
import com.pighand.aio.mapper.ECommerce.CouponMapper;
import com.pighand.aio.service.ECommerce.CouponService;
import com.pighand.aio.vo.ECommerce.CouponVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.page.PageType;
import org.springframework.stereotype.Service;

import static com.pighand.aio.domain.ECommerce.table.CouponUserTableDef.COUPON_USER;
import static com.pighand.aio.domain.base.table.StoreTableDef.STORE;

/**
 * 电商 - 优惠券
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Service
public class CouponServiceImpl extends BaseServiceImpl<CouponMapper, CouponDomain> implements CouponService {

    /**
     * 创建
     *
     * @param couponVO
     * @return
     */
    @Override
    public CouponVO create(CouponVO couponVO) {
        super.mapper.insert(couponVO);

        return couponVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public CouponDomain find(Long id) {
        return super.mapper.find(id, STORE.getTableName(), COUPON_USER.getTableName());
    }

    /**
     * 分页或列表
     *
     * @param couponVO
     * @return PageOrList<CouponVO>
     */
    @Override
    public PageOrList<CouponVO> query(CouponVO couponVO) {
        couponVO.setPageType(PageType.PAGE);

        return super.mapper.query(couponVO, null);
    }

    /**
     * 修改
     *
     * @param couponVO
     */
    @Override
    public void update(CouponVO couponVO) {
        super.mapper.update(couponVO);
    }

    /**
     * 删除
     *
     * @param id
     */
    @Override
    public void delete(Long id) {
        super.mapper.deleteById(id);
    }

}
