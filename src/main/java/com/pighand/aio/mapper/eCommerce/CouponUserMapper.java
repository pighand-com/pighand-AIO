package com.pighand.aio.mapper.ECommerce;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.CouponUserDomain;
import com.pighand.aio.vo.ECommerce.CouponUserVO;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.framework.spring.page.PageOrList;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

import static com.pighand.aio.domain.ECommerce.table.CouponTableDef.COUPON;
import static com.pighand.aio.domain.ECommerce.table.CouponUserTableDef.COUPON_USER;
import static com.pighand.aio.domain.user.table.UserExtensionTableDef.USER_EXTENSION;
import static com.pighand.aio.domain.user.table.UserTableDef.USER;

/**
 * 电商 - 优惠券 - 用户已领
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Mapper
public interface CouponUserMapper extends BaseMapper<CouponUserDomain> {

    /**
     * 基础查询条件
     *
     * @return
     */
    default QueryWrapper baseQuery(List<String> joinTables, QueryWrapper queryWrapper) {
        if (joinTables == null) {
            joinTables = new ArrayList<>();
        }

        if (queryWrapper == null) {
            queryWrapper = QueryWrapper.create();
        }

        queryWrapper.select(COUPON_USER.ID);

        if (joinTables.contains(COUPON.getTableName())) {
            queryWrapper.select(COUPON.NAME.as("coupon$name"), COUPON.LOGO);
            queryWrapper.leftJoin(COUPON).on(COUPON.ID.eq(COUPON_USER.COUPON_ID));
        }

        if (joinTables.contains(USER.getTableName())) {
            queryWrapper.select(USER.PHONE, USER_EXTENSION.PROFILE);
            queryWrapper.leftJoin(USER).as("owner").on(COUPON_USER.OWNER_ID.eq(USER.ID));
            queryWrapper.leftJoin(USER_EXTENSION).as("owner.extension").on(COUPON_USER.OWNER_ID.eq(USER_EXTENSION.ID));
        }

        return queryWrapper;
    }

    /**
     * 查询详情
     *
     * @param id         主键
     * @param joinTables 关联表
     * @return
     */
    default CouponUserVO find(Long id, List<String> joinTables) {
        QueryWrapper queryWrapper = this.baseQuery(joinTables, null).where(COUPON_USER.ID.eq(id));

        return this.selectOneByQueryAs(queryWrapper, CouponUserVO.class);
    }

    /**
     * 分页或列表
     *
     * @param couponUserDomain
     * @return
     */
    default PageOrList<CouponUserVO> query(CouponUserDomain couponUserDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.baseQuery(couponUserDomain.getJoinTables(), queryWrapper);

        return this.page(couponUserDomain, finalQueryWrapper, CouponUserVO.class);
    }
}
