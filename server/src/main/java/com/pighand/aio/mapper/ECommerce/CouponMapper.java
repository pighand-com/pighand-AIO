package com.pighand.aio.mapper.ECommerce;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.CouponDomain;
import com.pighand.aio.domain.ECommerce.CouponUserDomain;
import com.pighand.aio.vo.ECommerce.CouponUserVO;
import com.pighand.aio.vo.ECommerce.CouponVO;
import com.pighand.framework.spring.base.BaseMapper;
import com.pighand.framework.spring.page.PageOrList;
import com.pighand.framework.spring.util.BeanUtil;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.pighand.aio.domain.ECommerce.table.CouponTableDef.COUPON;
import static com.pighand.aio.domain.ECommerce.table.CouponUserTableDef.COUPON_USER;
import static com.pighand.aio.domain.base.table.StoreTableDef.STORE;

/**
 * 电商 - 优惠券
 *
 * @author wangshuli
 * @createDate 2024-06-05 17:35:51
 */
@Mapper
public interface CouponMapper extends BaseMapper<CouponDomain> {

    /**
     * 基础查询条件
     *
     * @return
     */
    default QueryWrapper relationOne(Set<String> joinTables, QueryWrapper queryWrapper) {
        if (queryWrapper == null) {
            queryWrapper = QueryWrapper.create();
        }

        if (joinTables == null || joinTables.isEmpty()) {
            return queryWrapper;
        }

        // STORE
        if (joinTables.contains(STORE.getTableName())) {
            queryWrapper.leftJoin(STORE).on(STORE.ID.eq(COUPON.STORE_ID));

            joinTables.remove(STORE.getTableName());
        }

        return queryWrapper;
    }

    /**
     * 一对多关联查询结果
     *
     * @return
     */
    default void relationMany(Set<String> joinTables, Object result) {
        if (joinTables == null || joinTables.isEmpty()) {
            return;
        }

        boolean isList = result instanceof List;

        List<Function<CouponVO, Long>> mainIdGetters = new ArrayList<>(joinTables.size());
        List<Function<Object, Long>> subTableIdGetter = new ArrayList<>(joinTables.size());
        List<BiConsumer<CouponVO, List>> subResultSetter = new ArrayList<>(joinTables.size());

        List<Function<Set<Long>, List>> subTableQueriesList = null;
        List<Function<Long, List>> subTableQueriesSingle = null;
        if (isList) {
            subTableQueriesList = new ArrayList<>(joinTables.size());
        } else {
            subTableQueriesSingle = new ArrayList<>(joinTables.size());
        }

        // COUPON_USER
        if (joinTables.contains(COUPON_USER.getTableName())) {
            mainIdGetters.add(CouponVO::getId);

            if (subTableQueriesList != null) {
                subTableQueriesList.add(ids -> new CouponUserDomain().select(COUPON_USER.DEFAULT_COLUMNS)
                    .where(COUPON_USER.COUPON_ID.in(ids)).listAs(CouponUserVO.class));
            } else {
                subTableQueriesSingle.add(
                    id -> new CouponUserDomain().select(COUPON_USER.DEFAULT_COLUMNS).where(COUPON_USER.COUPON_ID.eq(id))
                        .listAs(CouponUserVO.class));
            }

            subTableIdGetter.add(obj -> ((CouponUserVO)obj).getCouponId());
            subResultSetter.add((vo, list) -> vo.setCouponUser((List<CouponUserVO>)list));
        }

        if (result instanceof List) {
            BeanUtil.queryWithRelatedData((List)result, mainIdGetters, subTableQueriesList, subTableIdGetter,
                subResultSetter);
        } else {
            BeanUtil.queryWithRelatedData((CouponVO)result, mainIdGetters, subTableQueriesSingle, subResultSetter);
        }
    }

    /**
     * 查询详情
     *
     * @param id         主键
     * @param joinTables 关联表
     * @return
     */
    default CouponVO find(Long id, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper queryWrapper = this.relationOne(joinTableSet, null).where(COUPON.ID.eq(id));

        CouponVO result = this.selectOneByQueryAs(queryWrapper, CouponVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 查询详情
     *
     * @param queryWrapper
     * @param joinTables   关联表
     * @return
     */
    default CouponVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper finalQueryWrapper = this.relationOne(joinTableSet, queryWrapper);

        CouponVO result = this.selectOneByQueryAs(finalQueryWrapper, CouponVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 分页或列表
     *
     * @param couponDomain
     * @return
     */
    default PageOrList<CouponVO> query(CouponDomain couponDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(couponDomain.getJoinTables(), queryWrapper);

        PageOrList<CouponVO> result = this.page(couponDomain, finalQueryWrapper, CouponVO.class);
        this.relationMany(couponDomain.getJoinTables(), result.getRecords());

        return result;
    }
}
