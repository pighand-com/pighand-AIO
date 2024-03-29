package com.pighand.aio.service.eCommerce.impl;

import com.pighand.aio.domain.eCommerce.CouponDomain;
import com.pighand.aio.mapper.eCommerce.CouponMapper;
import com.pighand.aio.service.eCommerce.CouponService;
import com.pighand.aio.vo.eCommerce.CouponVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.page.PageType;
import org.springframework.stereotype.Service;

import java.util.List;

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
        List<String> joinTables = List.of("store", "coupon_user");

        return super.mapper.find(id, joinTables);
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

        return super.mapper.query(couponVO);
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
