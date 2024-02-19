package com.pighand.aio.mapper.eCommerce;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.aio.domain.eCommerce.CouponUserTransferDomain;
import com.pighand.aio.domain.eCommerce.table.CouponUserTransferTableDef;
import com.pighand.aio.vo.eCommerce.CouponUserTransferVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 电商 - 优惠券 - 转赠记录
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Mapper
public interface CouponUserTransferMapper extends BaseMapper<CouponUserTransferDomain> {

    CouponUserTransferTableDef COUPON_USER_TRANSFER = CouponUserTransferTableDef.COUPON_USER_TRANSFER;

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

        return queryWrapper;
    }

    /**
     * 查询详情
     *
     * @param id         主键
     * @param joinTables 关联表
     * @return
     */
    default CouponUserTransferVO find(Long id, List<String> joinTables) {
        QueryWrapper queryWrapper = this.baseQuery(joinTables).where(COUPON_USER_TRANSFER.ID.eq(id));

        return this.selectOneByQueryAs(queryWrapper, CouponUserTransferVO.class);
    }

    /**
     * 分页或列表
     *
     * @param couponUserTransferDomain
     * @return
     */
    default PageOrList<CouponUserTransferVO> query(CouponUserTransferDomain couponUserTransferDomain) {
        QueryWrapper queryWrapper = this.baseQuery(couponUserTransferDomain.getJoinTables());

        return this.page(couponUserTransferDomain, queryWrapper, CouponUserTransferVO.class);
    }
}
