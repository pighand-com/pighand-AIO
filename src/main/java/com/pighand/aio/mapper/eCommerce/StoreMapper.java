package com.pighand.aio.mapper.ECommerce;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.StoreDomain;
import com.pighand.aio.vo.ECommerce.StoreVO;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.framework.spring.page.PageOrList;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

import static com.pighand.aio.domain.ECommerce.table.CouponTableDef.COUPON;
import static com.pighand.aio.domain.ECommerce.table.StoreTableDef.STORE;

/**
 * 电商 - 门店
 *
 * @author wangshuli
 * @createDate 2023-12-04 16:37:26
 */
@Mapper
public interface StoreMapper extends BaseMapper<StoreDomain> {

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

        if (joinTables.contains(COUPON.getTableName())) {
            queryWrapper.leftJoin(COUPON).on(COUPON.STORE_ID.eq(STORE.ID));
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
    default StoreVO find(Long id, List<String> joinTables) {
        QueryWrapper queryWrapper = this.baseQuery(joinTables).where(STORE.ID.eq(id));

        return this.selectOneByQueryAs(queryWrapper, StoreVO.class);
    }

    /**
     * 分页或列表
     *
     * @param storeDomain
     * @return
     */
    default PageOrList<StoreVO> query(StoreDomain storeDomain) {
        QueryWrapper queryWrapper = this.baseQuery(storeDomain.getJoinTables());

        return this.page(storeDomain, queryWrapper, StoreVO.class);
    }
}
