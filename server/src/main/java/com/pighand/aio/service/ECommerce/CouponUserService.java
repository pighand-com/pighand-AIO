package com.pighand.aio.service.ECommerce;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.common.enums.CouponUserStatusEnum;
import com.pighand.aio.common.interceptor.Context;
import com.pighand.aio.domain.ECommerce.CouponUserDomain;
import com.pighand.aio.mapper.ECommerce.CouponUserMapper;
import com.pighand.aio.service.ECommerce.CouponUserTransferService;
import com.pighand.aio.vo.ECommerce.CouponUserTransferVO;
import com.pighand.aio.vo.ECommerce.CouponUserVO;
import com.pighand.framework.spring.base.BaseServiceImpl;
import com.pighand.framework.spring.exception.ThrowException;
import com.pighand.framework.spring.exception.ThrowPrompt;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.page.PageType;
import com.pighand.framework.spring.util.VerifyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

import static com.pighand.aio.domain.ECommerce.table.CouponTableDef.COUPON;
import static com.pighand.aio.domain.ECommerce.table.CouponUserTableDef.COUPON_USER;
import static com.pighand.aio.domain.base.table.UserTableDef.USER;

/**
 * 电商 - 优惠券 - 用户已领
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Service
public class CouponUserService extends BaseServiceImpl<CouponUserMapper, CouponUserDomain>
     {

    @Autowired
    private CouponUserTransferService couponUserTransferService;

    /**
     * 创建
     *
     * @param couponUserVO
     * @return
     */
    public CouponUserVO create(CouponUserVO couponUserVO) {
        couponUserVO.setStatus(CouponUserStatusEnum.UNUSED);
        super.mapper.insert(couponUserVO);

        return couponUserVO;
    }

    /**
     * 查询已领优惠券
     *
     * @param couponUserVO
     * @return PageOrList<CouponUserVO>
     */
    public PageOrList<CouponUserVO> query(CouponUserVO couponUserVO) {
        couponUserVO.setPageType(PageType.PAGE);
        couponUserVO.setJoinTables(COUPON.getTableName());

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.where(COUPON_USER.STATUS.eq(CouponUserStatusEnum.UNUSED));

        if (couponUserVO.getCreatorId() != null) {
            queryWrapper.where(COUPON_USER.OWNER_ID.eq(Context.loginUser().getId()));
        }

        return super.mapper.query(couponUserVO, queryWrapper);
    }

    /**
     * 转赠
     *
     * @param couponId
     * @param toUserId
     */
    @Transactional(rollbackFor = Exception.class)
    public void transfer(Long couponId, Long toUserId) {
        Long fromUserId = Context.loginUser().getId();
        if (fromUserId.equals(toUserId)) {
            return;
        }

        boolean isUpdate = this.updateChain().set(COUPON_USER.OWNER_ID, toUserId).where(
            COUPON_USER.ID.eq(couponId).and(COUPON_USER.OWNER_ID.eq(fromUserId))
                .and(COUPON_USER.STATUS.eq(CouponUserStatusEnum.UNUSED))).update();

        if (!isUpdate) {
            throw new ThrowException("赠送失败");
        }

        CouponUserTransferVO couponUserTransferVO = new CouponUserTransferVO();
        couponUserTransferVO.setCouponUserId(couponId);
        couponUserTransferVO.setFromUserId(fromUserId);
        couponUserTransferVO.setToUserId(toUserId);
        couponUserTransferVO.setCreatedAt(new Date());
        couponUserTransferService.create(couponUserTransferVO);
    }

    /**
     * 核销
     */
    public void verify(Long id, Long creatorId) {
        CouponUserDomain couponUserDomain =
            this.queryChain().where(COUPON_USER.ID.eq(id)).and(COUPON_USER.OWNER_ID.eq(creatorId)).one();
        if (couponUserDomain == null) {
            throw new ThrowPrompt("不存在");
        }

        if (!couponUserDomain.getStatus().equals(CouponUserStatusEnum.UNUSED)) {
            throw new ThrowPrompt("已使用");
        }

        boolean isUpdate =
            this.updateChain().set(COUPON_USER.STATUS, CouponUserStatusEnum.VERIFY).set(COUPON_USER.USED_AT, new Date())
                .where(COUPON_USER.ID.eq(id)).update();

        if (!isUpdate) {
            throw new ThrowPrompt("核销失败");
        }
    }

    /**
     * 查询核销记录
     *
     * @param couponUserVO
     * @return
     */
    public PageOrList<CouponUserVO> queryVerify(CouponUserVO couponUserVO) {
        couponUserVO.setPageType(PageType.PAGE);
        couponUserVO.setJoinTables(USER.getTableName(), COUPON.getTableName());

        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.select(COUPON_USER.ID, COUPON_USER.USED_AT);
        queryWrapper.where(COUPON_USER.STATUS.eq(CouponUserStatusEnum.VERIFY))
            .and(COUPON.APPLICATION_ID.eq(Context.applicationId()));

        if (VerifyUtils.isNotEmpty(couponUserVO.getQueryPhone())) {
            queryWrapper.where(USER.PHONE.like(couponUserVO.getQueryPhone()));
        }

        return super.mapper.query(couponUserVO, queryWrapper);
    }

}
