package com.pighand.aio.service.ECommerce.impl;

import com.pighand.aio.domain.ECommerce.CouponUserTransferDomain;
import com.pighand.aio.mapper.ECommerce.CouponUserTransferMapper;
import com.pighand.aio.service.ECommerce.CouponUserTransferService;
import com.pighand.aio.vo.ECommerce.CouponUserTransferVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.page.PageOrList;
import org.springframework.stereotype.Service;

/**
 * 电商 - 优惠券 - 转赠记录
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Service
public class CouponUserTransferServiceImpl extends BaseServiceImpl<CouponUserTransferMapper, CouponUserTransferDomain>
    implements CouponUserTransferService {

    /**
     * 创建
     *
     * @param couponUserTransferVO
     * @return
     */
    @Override
    public CouponUserTransferVO create(CouponUserTransferVO couponUserTransferVO) {
        super.mapper.insert(couponUserTransferVO);

        return couponUserTransferVO;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @Override
    public CouponUserTransferDomain find(Long id) {

        return super.mapper.find(id);
    }

    /**
     * 分页或列表
     *
     * @param couponUserTransferVO
     * @return PageOrList<CouponUserTransferVO>
     */
    @Override
    public PageOrList<CouponUserTransferVO> query(CouponUserTransferVO couponUserTransferVO) {
        return super.mapper.query(couponUserTransferVO, null);
    }

    /**
     * 修改
     *
     * @param couponUserTransferVO
     */
    @Override
    public void update(CouponUserTransferVO couponUserTransferVO) {
        super.mapper.update(couponUserTransferVO);
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
