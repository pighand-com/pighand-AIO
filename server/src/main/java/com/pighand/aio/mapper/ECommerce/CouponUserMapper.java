package com.pighand.aio.mapper.ECommerce;

import com.mybatisflex.core.query.QueryWrapper;
import com.pighand.aio.domain.ECommerce.CouponUserDomain;
import com.pighand.aio.domain.ECommerce.CouponUserTransferDomain;
import com.pighand.aio.vo.ECommerce.CouponUserTransferVO;
import com.pighand.aio.vo.ECommerce.CouponUserVO;
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
import static com.pighand.aio.domain.ECommerce.table.CouponUserTransferTableDef.COUPON_USER_TRANSFER;
import static com.pighand.aio.domain.ECommerce.table.OrderTableDef.ORDER;
import static com.pighand.aio.domain.base.table.UserTableDef.USER;

/**
 * 电商 - 优惠券 - 用户已领
 *
 * @author wangshuli
 * @createDate 2024-06-05 17:35:51
 */
@Mapper
public interface CouponUserMapper extends BaseMapper<CouponUserDomain> {

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

        // COUPON
        if (joinTables.contains(COUPON.getTableName())) {
            queryWrapper.leftJoin(COUPON).on(COUPON.ID.eq(COUPON_USER.COUPON_ID));

            joinTables.remove(COUPON.getTableName());
        }

        // ORDER
        if (joinTables.contains(ORDER.getTableName())) {
            queryWrapper.leftJoin(ORDER).on(ORDER.ID.eq(COUPON_USER.USED_ORDER_ID));

            joinTables.remove(ORDER.getTableName());
        }

        // USER
        if (joinTables.contains(USER.getTableName())) {
            queryWrapper.leftJoin(USER).on(USER.ID.eq(COUPON_USER.OWNER_ID));

            joinTables.remove(USER.getTableName());
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

        List<Function<CouponUserVO, Long>> mainIdGetters = new ArrayList<>(joinTables.size());
        List<Function<Object, Long>> subTableIdGetter = new ArrayList<>(joinTables.size());
        List<BiConsumer<CouponUserVO, List>> subResultSetter = new ArrayList<>(joinTables.size());

        List<Function<Set<Long>, List>> subTableQueriesList = null;
        List<Function<Long, List>> subTableQueriesSingle = null;
        if (isList) {
            subTableQueriesList = new ArrayList<>(joinTables.size());
        } else {
            subTableQueriesSingle = new ArrayList<>(joinTables.size());
        }

        // COUPON_USER_TRANSFER
        if (joinTables.contains(COUPON_USER_TRANSFER.getTableName())) {
            mainIdGetters.add(CouponUserVO::getId);

            if (subTableQueriesList != null) {
                subTableQueriesList.add(
                    ids -> new CouponUserTransferDomain().select(COUPON_USER_TRANSFER.DEFAULT_COLUMNS)
                        .where(COUPON_USER_TRANSFER.COUPON_USER_ID.in(ids)).listAs(CouponUserTransferVO.class));
            } else {
                subTableQueriesSingle.add(
                    id -> new CouponUserTransferDomain().select(COUPON_USER_TRANSFER.DEFAULT_COLUMNS)
                        .where(COUPON_USER_TRANSFER.COUPON_USER_ID.eq(id)).listAs(CouponUserTransferVO.class));
            }

            subTableIdGetter.add(obj -> ((CouponUserTransferVO)obj).getCouponUserId());
            subResultSetter.add((vo, list) -> vo.setCouponUserTransfer((List<CouponUserTransferVO>)list));
        }

        if (result instanceof List) {
            BeanUtil.queryWithRelatedData((List)result, mainIdGetters, subTableQueriesList, subTableIdGetter,
                subResultSetter);
        } else {
            BeanUtil.queryWithRelatedData((CouponUserVO)result, mainIdGetters, subTableQueriesSingle, subResultSetter);
        }
    }

    /**
     * 查询详情
     *
     * @param id         主键
     * @param joinTables 关联表
     * @return
     */
    default CouponUserVO find(Long id, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper queryWrapper = this.relationOne(joinTableSet, null).where(COUPON_USER.ID.eq(id));

        CouponUserVO result = this.selectOneByQueryAs(queryWrapper, CouponUserVO.class);
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
    default CouponUserVO find(QueryWrapper queryWrapper, String... joinTables) {
        Set<String> joinTableSet = Stream.of(joinTables).collect(Collectors.toSet());

        QueryWrapper finalQueryWrapper = this.relationOne(joinTableSet, queryWrapper);

        CouponUserVO result = this.selectOneByQueryAs(finalQueryWrapper, CouponUserVO.class);
        this.relationMany(joinTableSet, result);

        return result;
    }

    /**
     * 分页或列表
     *
     * @param couponUserDomain
     * @return
     */
    default PageOrList<CouponUserVO> query(CouponUserDomain couponUserDomain, QueryWrapper queryWrapper) {
        QueryWrapper finalQueryWrapper = this.relationOne(couponUserDomain.getJoinTables(), queryWrapper);

        PageOrList<CouponUserVO> result = this.page(couponUserDomain, finalQueryWrapper, CouponUserVO.class);
        this.relationMany(couponUserDomain.getJoinTables(), result.getRecords());

        return result;
    }
}
