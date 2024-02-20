package com.pighand.aio.mapper.eCommerce;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.eCommerce.CouponDomain;
import com.pighand.aio.vo.eCommerce.CouponVO;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.framework.spring.page.PageOrList;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

import static com.mybatisflex.core.query.QueryMethods.count;
import static com.pighand.aio.domain.eCommerce.table.CouponTableDef.COUPON;
import static com.pighand.aio.domain.eCommerce.table.CouponUserTableDef.COUPON_USER;
import static com.pighand.aio.domain.eCommerce.table.StoreTableDef.STORE;

/**
 * 电商 - 优惠券
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Mapper
public interface CouponMapper extends BaseMapper<CouponDomain> {

    /**
     * 基础查询条件
     *
     * @return
     */
    default QueryWrapper baseQuery(List<String> joinTables) {
        if (joinTables == null) {
            joinTables = new ArrayList<>();
        }

        QueryWrapper queryWrapper = QueryWrapper.create();
        queryWrapper.select(COUPON.ID, COUPON.NAME, COUPON.LOGO, count(COUPON_USER.ID).as("holdCount"));

        queryWrapper.leftJoin(COUPON_USER).on(COUPON_USER.COUPON_ID.eq(COUPON.ID));

        if (joinTables.contains(STORE.getTableName())) {
            queryWrapper.leftJoin(STORE).on(STORE.ID.eq(COUPON.STORE_ID));
        }

        queryWrapper.groupBy(COUPON.ID);

        return queryWrapper;
    }

    /**
     * 查询详情
     *
     * @param id         主键
     * @param joinTables 关联表
     * @return
     */
    default CouponVO find(Long id, List<String> joinTables) {
        QueryWrapper queryWrapper = this.baseQuery(joinTables).where(COUPON.ID.eq(id));

        return this.selectOneByQueryAs(queryWrapper, CouponVO.class);
    }

    /**
     * 分页或列表
     *
     * @param couponDomain
     * @return
     */
    default PageOrList<CouponVO> query(CouponDomain couponDomain) {
        QueryWrapper queryWrapper = this.baseQuery(couponDomain.getJoinTables());

        return this.page(couponDomain, queryWrapper, CouponVO.class);
    }
}
